package com.example.gestor_de_pagamentos.repository;

import com.example.gestor_de_pagamentos.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    // O Spring Data interpreta o nome do método e gera a query automaticamente:
    // SELECT * FROM faturas WHERE cliente_id = ?
    List<Fatura> findByClienteId(Long clienteId);
}
