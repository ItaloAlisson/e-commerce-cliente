package com.ecommerce.cliente.controllers;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<String> registrarCliente(@RequestBody @Valid ClienteRecordDTO clienteDTO) {

        return clienteService.registrarCliente(clienteDTO);
    }




}
