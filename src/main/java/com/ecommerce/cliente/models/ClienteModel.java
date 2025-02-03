package com.ecommerce.cliente.models;

import com.ecommerce.cliente.embedded.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity()
@Table(name = "CLIENTES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteModel implements Serializable {

    private static final long serialversionUID = 4L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private LocalDate dataNascimento;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Embedded
    private Endereco endereco;
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean ativo = true;

}

