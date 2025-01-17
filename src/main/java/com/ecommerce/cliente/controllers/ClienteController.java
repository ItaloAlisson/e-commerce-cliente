package com.ecommerce.cliente.controllers;

import com.ecommerce.cliente.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
}
