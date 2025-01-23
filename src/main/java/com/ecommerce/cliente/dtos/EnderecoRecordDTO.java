package com.ecommerce.cliente.dtos;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRecordDTO(
        @NotBlank(message = "Campo 'logradouro' não pode estar vazio!")
        String logradouro,
        @NotBlank(message = "Campo 'numero' não pode estar vazio!")
        String numero,
        @NotBlank(message = "Campo 'bairro' não pode estar vazio!")
        String bairro,
        @NotBlank(message = "Campo 'cidade' não pode estar vazio!")
        String cidade,
        @NotBlank(message = "Campo 'estado' não pode estar vazio!")
        String estado,
        @NotBlank(message = "Campo 'cep' não pode estar vazio!")
        String cep) {
}
