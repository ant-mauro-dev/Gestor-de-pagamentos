package com.example.gestor_de_pagamentos.service;

import com.example.gestor_de_pagamentos.model.Cliente;
import com.example.gestor_de_pagamentos.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    // Injeção de dependência via construtor.
    // O Spring cria uma instância do ClienteRepository e passa aqui automaticamente.
    // Essa abordagem é preferível ao @Autowired no campo, pois facilita testes.
    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        // findById retorna um Optional<Cliente>.
        // orElseThrow lança uma exceção se o Optional estiver vazio (registro não encontrado).
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {
        // Reutilizamos buscarPorId para garantir que o cliente existe antes de atualizar.
        // Se não existir, a exceção já é lançada aqui.
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        return repository.save(cliente);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
