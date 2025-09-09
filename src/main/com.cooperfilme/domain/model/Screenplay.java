package com.cooperfilme.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="screenplays")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Screenplay {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=10000)
    private String text;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private ScreenplayStatus status;

    // Quem assumiu em cada etapa
    @ManyToOne(fetch=FetchType.LAZY) private User assignedTo;

    // Dados do cliente
    @Column(nullable=false) private String clientName;
    @Column(nullable=false) private String clientEmail;
    @Column(nullable=false) private String clientPhone;

    // Justificativas do Analista
    private String analysisJustification; // por que boa/ruim ideia

    // Comentários do Revisor
    private String reviewNotes;

    // Datas
    @Column(nullable=false) private Instant createdAt;
    private Instant updatedAt;

    // Token simples de consulta pública pelo cliente
    @Column(nullable=false, unique=true) private String publicToken;

    @OneToMany(mappedBy="screenplay", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Vote> votes = new ArrayList<>();
}