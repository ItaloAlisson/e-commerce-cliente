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

    private static final long serialversionUID = 2L;
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
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deletado = false;

    public ClienteModel() {
    }

    public ClienteModel(UUID id, String nome, String dataNascimento, String email, String cpf, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.cpf = cpf;
        this.endereco = endereco;
    }
}

