package com.cooperfilme.infra.config;

import com.cooperfilme.domain.model.Role;
import com.cooperfilme.domain.model.User;
import com.cooperfilme.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        repo.save(User.builder().name("Ana Analista").email("analista@cooperfilme.com")
            .role(Role.ANALISTA).passwordHash(encoder.encode("analista123")).active(true).build());

        repo.save(User.builder().name("Rui Revisor").email("revisor@cooperfilme.com")
            .role(Role.REVISOR).passwordHash(encoder.encode("revisor123")).active(true).build());

        repo.save(User.builder().name("Ava Aprovadora 1").email("aprov1@cooperfilme.com")
            .role(Role.APROVADOR).passwordHash(encoder.encode("aprov123")).active(true).build());
        repo.save(User.builder().name("Aldo Aprovador 2").email("aprov2@cooperfilme.com")
            .role(Role.APROVADOR).passwordHash(encoder.encode("aprov123")).active(true).build());
        repo.save(User.builder().name("Aura Aprovadora 3").email("aprov3@cooperfilme.com")
            .role(Role.APROVADOR).passwordHash(encoder.encode("aprov123")).active(true).build());
    }
}