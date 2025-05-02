package com.bcb.bcb.repository;

import com.bcb.bcb.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByDocumentId(String documentoId);
}
