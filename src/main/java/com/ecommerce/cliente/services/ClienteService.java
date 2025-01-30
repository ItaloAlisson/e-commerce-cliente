package com.ecommerce.cliente.services;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.exceptions.ResourceNotFoundException;
import com.ecommerce.cliente.mappers.ClienteMapper;
import com.ecommerce.cliente.models.ClienteModel;
import com.ecommerce.cliente.repositories.ClienteRepository;
import com.ecommerce.cliente.validation.ClienteValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteValidator clienteValidator;

    @Autowired
    private ClienteMapper clienteMapper;

    @CacheEvict(value = "clientes", allEntries = true)
    @Transactional
    public ClienteModel registrarCliente(ClienteRecordDTO clienteDTO) {
        clienteValidator.existePorCpf(clienteDTO.cpf());
        clienteValidator.existePorEmail(clienteDTO.email());
        var novoCliente = clienteMapper.clienteDTOParaModel(clienteDTO);
        var endereco = clienteMapper.enderecoDTOParaEndereco(clienteDTO.endereco());
        novoCliente.setEndereco(endereco);
        return clienteRepository.save(novoCliente);
    }

    @Cacheable(value = "clientes", key = "#paginado.pageNumber + '-' + #paginado.pageSize")
    public Page<ClienteModel> buscarClientesAtivos(Pageable paginado) {
        return clienteRepository.findByAtivoTrue(paginado);
    }

    @Cacheable(value = "clientes", key = "#cpf")
    public ClienteModel buscarClienteAtivoPorCpf(String cpf) {
        Optional<ClienteModel> cliente = clienteRepository.findByCpfAndAtivoTrue(cpf);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        throw new ResourceNotFoundException("Cliente com o CPF " + cpf
                + " não foi encontrado.");
    }

    @Cacheable(value = "clientes", key = "#paginado.pageNumber + '-' + #paginado.pageSize")
    public Page<ClienteModel> buscarClientesInativos(Pageable paginado) {
        return clienteRepository.findByAtivoFalse(paginado);
    }

    @Cacheable(value = "clientes", key = "#cpf")
    public ClienteModel buscarClienteInativoPorCpf(String cpf) {
        Optional<ClienteModel> cliente = clienteRepository.findByCpfAndAtivoFalse(cpf);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        throw new ResourceNotFoundException("Cliente com o CPF " + cpf
                + " não foi encontrado.");
    }

   @CacheEvict(value = "clientes", allEntries = true)
    @Transactional
    public ClienteModel atualizarDadosCliente(UUID id, ClienteRecordDTO clienteDTO) {
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            var clienteAtualizado = clienteMapper.clienteDTOParaModel(clienteDTO);
            clienteAtualizado.setId(id);
            clienteRepository.save(clienteAtualizado);
            return clienteAtualizado;
        }
        throw new ResourceNotFoundException("Cliente com o ID " + id
                + " não foi encontrado.");
    }

    @CacheEvict(value = "clientes", allEntries = true)
    @Transactional
    public void alternarStatusCliente(UUID id, ClienteStatusRecordDTO clienteStatusDTO) {
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            var clienteStatusAtualizado = clienteOptional.get();
            clienteStatusAtualizado.setAtivo(clienteStatusDTO.ativo());
            clienteRepository.save(clienteStatusAtualizado);
            return;
        }
        throw new ResourceNotFoundException("Cliente com o ID " + id
                + " não foi encontrado.");
    }

    @CacheEvict(value = "clientes", allEntries = true)
    @Transactional
    public void deletarCliente(UUID id) {
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            clienteRepository.delete(clienteOptional.get());
            return;
        }
        throw new ResourceNotFoundException("Cliente com o ID " + id
                + " não foi encontrado.");

    }
}