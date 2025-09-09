package com.cooperfilme.application.controller;

import com.cooperfilme.application.dto.*;
import com.cooperfilme.application.security.JwtUtil;
import com.cooperfilme.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest req) {
        var user = userRepo.findByEmail(req.email()).orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
        if (!encoder.matches(req.password(), user.getPasswordHash())) throw new RuntimeException("Credenciais inválidas");
        String token = jwt.generate(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getName(), user.getRole().name());
    }
}