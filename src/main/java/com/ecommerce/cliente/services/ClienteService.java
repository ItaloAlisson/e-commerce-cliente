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

    @Transactional
    public ClienteModel registrarCliente(ClienteRecordDTO clienteDTO) {
        clienteValidator.existePorCpf(clienteDTO.cpf());
        clienteValidator.existePorEmail(clienteDTO.email());
        var novoCliente = clienteMapper.clienteDTOParaModel(clienteDTO);
        var endereco = clienteMapper.enderecoDTOParaEndereco(clienteDTO.endereco());
        novoCliente.setEndereco(endereco);
        return clienteRepository.save(novoCliente);
    }

    public ResponseEntity<Page<ClienteModel>> buscarClientesAtivos(Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteRepository.findByAtivoTrue(paginado));
    }

    public ResponseEntity<ClienteModel> buscarClienteAtivoPorCpf(String cpf) {
        Optional<ClienteModel> cliente = clienteRepository.findByCpfAndAtivoTrue(cpf);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        throw new ResourceNotFoundException("Cliente com o CPF " + cpf
                + " não foi encontrado.");
    }

    public ResponseEntity<Page<ClienteModel>> buscarClientesInativos(Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteRepository.findByAtivoFalse(paginado));
    }

    public ResponseEntity<ClienteModel> buscarClienteInativoPorCpf(String cpf) {
        Optional<ClienteModel> cliente = clienteRepository.findByCpfAndAtivoFalse(cpf);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        throw new ResourceNotFoundException("Cliente com o CPF " + cpf
                + " não foi encontrado.");
    }

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