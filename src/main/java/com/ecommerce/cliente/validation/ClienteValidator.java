package com.ecommerce.cliente.validation;

import com.ecommerce.cliente.exceptions.ConflictException;
import com.ecommerce.cliente.repositories.ClienteRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteValidator {
    @Autowired
    private ClienteRepository clienteRepository;

    public void existePorCpf(String cpf){

       if (clienteRepository.existsByCpf(cpf)){
           throw new ConflictException("CPF já cadastrado");
       }
    }

    public void existePorEmail(String email){
        if (clienteRepository.existsByEmail(email)){
            throw new ConflictException("E-mail já cadastrado!");
        }

    }
}
