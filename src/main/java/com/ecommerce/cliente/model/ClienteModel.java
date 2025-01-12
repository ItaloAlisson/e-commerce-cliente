package com.ecommerce.cliente.model;

import com.ecommerce.cliente.embedded.Endereco;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity()
@Table(name = "CLIENTES")
@Getter
@Setter
public class ClienteModel implements Serializable {

    private static final long serialversionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String dataNascimento;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Embedded
    private Endereco endereco;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer status;

    public ClienteModel() {
    }

    public ClienteModel(UUID id, String nome, String dataNascimento, String email, String cpf, Integer status) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.cpf = cpf;
        this.status = status;
    }
}

