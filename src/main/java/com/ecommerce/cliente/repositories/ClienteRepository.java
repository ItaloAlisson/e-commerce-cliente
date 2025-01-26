package com.ecommerce.cliente.repositories;

import com.ecommerce.cliente.models.ClienteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<ClienteModel> findByAtivoTrue(Pageable paginado);

    Optional<ClienteModel> findByCpfAndAtivoTrue(String cpf);

    Page<ClienteModel> findByAtivoFalse(Pageable paginado);
}
