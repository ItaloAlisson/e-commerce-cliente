package com.ecommerce.cliente.dtos;

import com.ecommerce.cliente.embedded.Endereco;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteRecordDTO(@NotBlank(message ="Campo 'nome' não pode estar vazio!")
                               String nome,
                               @NotNull(message ="Campo 'dataNascimento' não pode ser nulo!")
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                               LocalDate dataNascimento,
                               @NotBlank(message ="Campo 'email' não pode estar vazio!")
                               @Email(message = "O e-mail fornecido não é válido")
                               String email,
                               @NotBlank(message ="Campo 'cpf' não pode estar vazio!")
                               @CPF(message = "CPF inválido!")
                               String cpf,
                               @Valid
                               Endereco endereco,
                               boolean deletado) {
}
