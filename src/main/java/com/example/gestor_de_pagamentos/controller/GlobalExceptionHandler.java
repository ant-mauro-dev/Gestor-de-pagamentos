package com.example.gestor_de_pagamentos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Credenciais inválidas no login (email/senha errados) devem retornar 401,
    // não 404. AuthenticationException é lançada pelo AuthenticationManager.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", "Email ou senha invalidos"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // @ExceptionHandler define qual tipo de exceção este método vai interceptar.
    // Quando qualquer controller lançar uma RuntimeException genérica (ex: "nao encontrado"),
    // este método é chamado. Como AuthenticationException também é uma RuntimeException,
    // o handler mais específico acima precisa vir declarado antes deste no Spring.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
