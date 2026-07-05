package com.example.gestor_de_pagamentos.controller;

import com.example.gestor_de_pagamentos.model.Cliente;
import com.example.gestor_de_pagamentos.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes") // prefixo comum a todos os endpoints desta classe
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(service.listarTodos()); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        // @PathVariable extrai o {id} da URL e injeta no parâmetro do método
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        // @RequestBody desserializa o JSON do corpo da requisição para um objeto Cliente
        Cliente salvo = service.salvar(cliente);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()      // pega a URL atual: /api/v1/clientes
                .path("/{id}")             // adiciona /{id}
                .buildAndExpand(salvo.getId()) // substitui {id} pelo ID gerado
                .toUri();
        return ResponseEntity.created(location).body(salvo); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
