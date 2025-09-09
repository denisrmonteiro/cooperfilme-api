package com.cooperfilme.application.controller;

import com.cooperfilme.application.dto.ScreenplayFilter;
import com.cooperfilme.domain.model.Screenplay;
import com.cooperfilme.domain.model.ScreenplayStatus;
import com.cooperfilme.domain.repository.ScreenplayRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/screenplays")
@RequiredArgsConstructor
public class ScreenplayQueryController {

    private final ScreenplayRepository repo;

    @GetMapping
    public List<Screenplay> list(
        @RequestParam(required=false) ScreenplayStatus status,
        @RequestParam(required=false) String email,
        @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        Specification<Screenplay> spec = (root, q, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (status != null) ps.add(cb.equal(root.get("status"), status));
            if (email != null) ps.add(cb.equal(root.get("clientEmail"), email));
            if (from != null) ps.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            if (to != null) ps.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
            return cb.and(ps.toArray(new Predicate[0]));
        };
        return repo.findAll(spec);
    }

    @GetMapping("/{id}")
    public Screenplay get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}