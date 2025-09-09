package com.cooperfilme.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Role role;

    @Column(nullable=false) private String passwordHash; // BCrypt
    private boolean active = true;
}