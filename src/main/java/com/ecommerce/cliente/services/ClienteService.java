package com.ecommerce.cliente.services;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.embedded.Endereco;
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
        ClienteModel clienteModel = clienteMapper.dtoParaModel(clienteDTO);
        Endereco endereco = clienteMapper.enderecoDTOParaEndereco(clienteDTO.endereco());
        clienteModel.setEndereco(endereco);
       clienteRepository.save(clienteModel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente registrado com sucesso!");
    }

    public ResponseEntity<Page<ClienteModel>> buscarClientesAtivos(Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.findByAtivoTrue(paginado));
    }
}
