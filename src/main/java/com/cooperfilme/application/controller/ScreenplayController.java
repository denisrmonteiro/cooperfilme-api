package com.cooperfilme.application.controller;

import com.cooperfilme.application.dto.*;
import com.cooperfilme.domain.model.Screenplay;
import com.cooperfilme.domain.service.ScreenplayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/screenplays")
@RequiredArgsConstructor
public class ScreenplayController {

    private final ScreenplayService service;

    // Público: envio
    @PostMapping
    public Screenplay create(@RequestBody @Valid CreateScreenplayRequest req) {
        return service.create(req);
    }

    // Público: consulta por token
    @GetMapping("/status")
    public PublicStatusResponse status(@RequestParam String token) {
        Screenplay s = service.getByPublicToken(token);
        return new PublicStatusResponse(s.getId(), s.getStatus().name(),
            s.getUpdatedAt() == null ? null : DateTimeFormatter.ISO_INSTANT.format(s.getUpdatedAt()));
    }

    // Protegido: assumir (opcional para UI)
    @PostMapping("/{id}/assumir")
    public Screenplay assume(@PathVariable Long id, @RequestParam Long userId) {
        return service.assume(id, userId);
    }

    // 1 -> 2 (qualquer usuário autenticado pode iniciar análise)
    @PostMapping("/{id}/transicoes/para-em-analise")
    public Screenplay toEmAnalise(@PathVariable Long id, @RequestParam Long userId) {
        return service.moveFrom1To2(id, userId);
    }

    // 2 -> 3 ou 8 (Analista)
    @PostMapping("/{id}/transicoes/analista-decisao")
    public Screenplay analystDecision(@PathVariable Long id, @RequestParam Long userId,
                                      @RequestBody @Valid AnalysisDecisionRequest req) {
        return service.analystDecision(id, userId, req);
    }

    // 3 -> 4 (Revisor)
    @PostMapping("/{id}/transicoes/para-em-revisao")
    public Screenplay toEmRevisao(@PathVariable Long id, @RequestParam Long userId) {
        return service.moveFrom3To4(id, userId);
    }

    // 4 -> 5 (Revisor)
    @PostMapping("/{id}/transicoes/revisor-decisao")
    public Screenplay reviewerDecision(@PathVariable Long id, @RequestParam Long userId,
                                       @RequestBody @Valid ReviewDecisionRequest req) {
        return service.reviewerDecision(id, userId, req);
    }

    // 5/6 -> 6/7/8 (Aprovadores)
    @PostMapping("/{id}/votos")
    public Screenplay vote(@PathVariable Long id, @RequestParam Long userId,
                           @RequestBody @Valid VoteRequest req) {
        return service.vote(id, userId, req);
    }
}