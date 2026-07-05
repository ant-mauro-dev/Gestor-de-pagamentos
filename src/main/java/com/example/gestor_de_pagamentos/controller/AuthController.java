package com.example.gestor_de_pagamentos.controller;

import com.example.gestor_de_pagamentos.dto.LoginRequest;
import com.example.gestor_de_pagamentos.dto.LoginResponse;
import com.example.gestor_de_pagamentos.dto.RegisterRequest;
import com.example.gestor_de_pagamentos.model.Usuario;
import com.example.gestor_de_pagamentos.repository.UsuarioRepository;
import com.example.gestor_de_pagamentos.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Rota pública: cria um novo usuário com a senha já em hash (BCrypt).
    @PostMapping("/register")
    public ResponseEntity<Void> registrar(@RequestBody RegisterRequest dados) {
        if (usuarioRepository.findByEmail(dados.email()).isPresent()) {
            throw new RuntimeException("Ja existe um usuario com este email");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        // Nunca salvamos a senha em texto puro: o BCrypt gera um hash irreversível.
        usuario.setSenha(passwordEncoder.encode(dados.senha()));

        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }

    // Rota pública: valida email/senha e devolve um token JWT.
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dados) {
        // authenticate() dispara o DaoAuthenticationProvider configurado no SecurityConfig,
        // que busca o usuário (via CustomUserDetailsService) e compara a senha com o hash salvo.
        // Se as credenciais forem inválidas, uma exceção é lançada automaticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.email(), dados.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dados.email())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
