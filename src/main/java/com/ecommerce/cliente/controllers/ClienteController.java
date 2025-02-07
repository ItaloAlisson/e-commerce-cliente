package com.ecommerce.cliente.controllers;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.models.ClienteModel;
import com.ecommerce.cliente.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    private  ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping()
    public ResponseEntity<ClienteModel> registrarCliente(@RequestBody @Valid ClienteRecordDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.registrarCliente(clienteDTO));
    }

    @GetMapping()
    public ResponseEntity<Page<ClienteModel>> buscarClientesAtivos(@PageableDefault(page = 0, size = 10,
            sort = "dataNascimento", direction = Sort.Direction.ASC) Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.buscarClientesAtivos(paginado));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteModel> buscarClienteAtivoPorCpf(@PathVariable(value = "cpf") String cpf) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.buscarClienteAtivoPorCpf(cpf));
    }

    @GetMapping("/inativos")
    public ResponseEntity<Page<ClienteModel>> buscarClientesInativos(@PageableDefault(page = 0, size = 10,
            sort = "dataNascimento", direction = Sort.Direction.ASC) Pageable paginado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.buscarClientesInativos(paginado));
    }

    @GetMapping("/inativo/{cpf}")
    public ResponseEntity<ClienteModel> buscarClienteInativoPorCpf(@PathVariable(value = "cpf") String cpf) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.buscarClienteInativoPorCpf(cpf));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteModel> atualizarDadosCliente(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid ClienteRecordDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.atualizarDadosCliente(id, clienteDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alternarStatusCliente(@PathVariable(value = "id") UUID id,
                                                      @RequestBody ClienteStatusRecordDTO clienteStatusDTO) {
        clienteService.alternarStatusCliente(id, clienteStatusDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable(value = "id") UUID id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
