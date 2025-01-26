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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registrarCliente(ClienteRecordDTO clienteDTO) {
        clienteValidator.existePorCpf(clienteDTO.cpf());
        clienteValidator.existePorEmail(clienteDTO.email());
        var novoCliente = clienteMapper.clienteDTOParaModel(clienteDTO);
        var endereco = clienteMapper.enderecoDTOParaEndereco(clienteDTO.endereco());
        novoCliente.setEndereco(endereco);
       clienteRepository.save(novoCliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente registrado com sucesso!");
    }

    public ResponseEntity<Page<ClienteModel>> buscarClientesAtivos(Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteRepository.findByAtivoTrue(paginado));
    }

    public ResponseEntity<ClienteModel> buscarClienteAtivoPorCpf(String cpf) {
        Optional<ClienteModel> cliente = clienteRepository.findByCpfAndAtivoTrue(cpf);
        if (cliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(cliente.get());
        }
        throw new ResourceNotFoundException("Cliente com o CPF " + cpf
                + " não foi encontrado.");
    }

    public ResponseEntity<String> atualizarDadosCliente(UUID id, ClienteRecordDTO clienteDTO){
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            var clienteAtualizado = clienteMapper.clienteDTOParaModel(clienteDTO);
            clienteAtualizado.setId(id);
            clienteRepository.save(clienteAtualizado);
            return ResponseEntity.status(HttpStatus.OK).body("Dados do cliente atualizados com sucesso!");
        }
        throw new ResourceNotFoundException("Cliente com o ID " + id
                + " não foi encontrado.");
    }

    public ResponseEntity<String> alternarStatusCliente(UUID id, ClienteStatusRecordDTO clienteStatusDTO) {
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            var clienteStatusAtualizado = clienteOptional.get();
            clienteStatusAtualizado.setAtivo(clienteStatusDTO.ativo());
            clienteRepository.save(clienteStatusAtualizado);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new ResourceNotFoundException("Cliente com o ID " + id
                + " não foi encontrado.");
    }
}