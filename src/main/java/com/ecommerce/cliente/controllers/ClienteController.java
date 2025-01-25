package com.ecommerce.cliente.controllers;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.models.ClienteModel;
import com.ecommerce.cliente.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<String> registrarCliente(@RequestBody @Valid ClienteRecordDTO clienteDTO) {
        return clienteService.registrarCliente(clienteDTO);
    }

    @GetMapping()
    public ResponseEntity<Page<ClienteModel>> buscarClientesAtivos(@PageableDefault(page = 0, size = 10,
            sort = "dataNascimento", direction = Sort.Direction.ASC) Pageable paginado) {
        return clienteService.buscarClientesAtivos(paginado);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteModel> buscarClientePorCpf(@PathVariable(value = "cpf") String cpf){
        return clienteService.buscarClientePorCpf(cpf);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarDadosCliente(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid ClienteRecordDTO clienteDTO){
        return clienteService.atualizarDadosCliente(id,clienteDTO);
    }


}
