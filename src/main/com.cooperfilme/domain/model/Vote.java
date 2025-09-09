package com.cooperfilme.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="votes",
    uniqueConstraints=@UniqueConstraint(columnNames={"screenplay_id","approver_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vote {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="screenplay_id")
    private Screenplay screenplay;

    @ManyToOne(optional=false) @JoinColumn(name="approver_id")
    private User approver;

    @Column(nullable=false)
    private Boolean approved; // true = aprovado, false = não

    @Column(length=1000)
    private String note;

    @Column(nullable=false)
    private Instant createdAt;
}