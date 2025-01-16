package com.ecommerce.cliente.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Endereco {
    @NotBlank(message ="Campo 'logradouro' não pode estar vazio!")
    @Column(nullable = false)
    private String logradouro;
    @NotBlank(message ="Campo 'numero' não pode estar vazio!")
    @Column(nullable = false)
    private String numero;
    @NotBlank(message ="Campo 'bairro' não pode estar vazio!")
    @Column(nullable = false)
    private String bairro;
    @NotBlank(message ="Campo 'cidade' não pode estar vazio!")
    @Column(nullable = false)
    private String cidade;
    @NotBlank(message ="Campo 'estado' não pode estar vazio!")
    @Column(nullable = false)
    private String estado;
    @NotBlank(message ="Campo 'cep' não pode estar vazio!")
    @Column(nullable = false)
    private String cep;


}
