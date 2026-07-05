package com.example.gestor_de_pagamentos.dto;

// DTO (Data Transfer Object): representa exatamente os dados que o endpoint
// de login espera receber, sem expor a entidade Usuario diretamente.
public record LoginRequest(String email, String senha) {
}
