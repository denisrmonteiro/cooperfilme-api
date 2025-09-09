// domain/service/ScreenplayService.java
package com.cooperfilme.domain.service;

import com.cooperfilme.domain.model.*;
import com.cooperfilme.domain.repository.*;
import com.cooperfilme.application.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScreenplayService {

    private final ScreenplayRepository screenplayRepo;
    private final UserRepository userRepo;
    private final VoteRepository voteRepo;

    @Transactional
    public Screenplay create(CreateScreenplayRequest req) {
        Screenplay s = Screenplay.builder()
            .text(req.text())
            .clientName(req.clientName())
            .clientEmail(req.clientEmail())
            .clientPhone(req.clientPhone())
            .status(ScreenplayStatus.AGUARDANDO_ANALISE)
            .publicToken(UUID.randomUUID().toString())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
        return screenplayRepo.save(s);
    }

    @Transactional
    public Screenplay assume(Long screenplayId, Long userId) {
        Screenplay s = get(screenplayId);
        User u = getUser(userId);
        // Qualquer usuário pode assumir o roteiro para a próxima ação?
        // Regras: Assumir em 1 -> enviar para 2; 3 -> revisor; etc.
        s.setAssignedTo(u);
        s.setUpdatedAt(Instant.now());
        return s;
    }

    @Transactional
    public Screenplay moveFrom1To2(Long screenplayId, Long userId) {
        Screenplay s = get(screenplayId);
        User u = validateRole(userId, null); // qualquer um pode assumir para análise
        ensureStatus(s, ScreenplayStatus.AGUARDANDO_ANALISE);
        s.setAssignedTo(u);
        s.setStatus(ScreenplayStatus.EM_ANALISE);
        s.setUpdatedAt(Instant.now());
        return s;
    }

    @Transactional
    public Screenplay analystDecision(Long id, Long analystId, AnalysisDecisionRequest req) {
        Screenplay s = get(id);
        User analyst = validateRole(analystId, Role.ANALISTA);
        ensureStatus(s, ScreenplayStatus.EM_ANALISE);

        s.setAnalysisJustification(req.justification());
        if (req.sendToReview()) {
            s.setStatus(ScreenplayStatus.AGUARDANDO_REVISAO);
        } else {
            s.setStatus(ScreenplayStatus.RECUSADO);
        }
        s.setAssignedTo(analyst);
        s.setUpdatedAt(Instant.now());
        return s;
    }

    @Transactional
    public Screenplay moveFrom3To4(Long id, Long reviewerId) {
        Screenplay s = get(id);
        User reviewer = validateRole(reviewerId, Role.REVISOR);
        ensureStatus(s, ScreenplayStatus.AGUARDANDO_REVISAO);

        s.setAssignedTo(reviewer);
        s.setStatus(ScreenplayStatus.EM_REVISAO);
        s.setUpdatedAt(Instant.now());
        return s;
    }

    @Transactional
    public Screenplay reviewerDecision(Long id, Long reviewerId, ReviewDecisionRequest req) {
        Screenplay s = get(id);
        User reviewer = validateRole(reviewerId, Role.REVISOR);
        ensureStatus(s, ScreenplayStatus.EM_REVISAO);

        s.setReviewNotes(req.notes());
        s.setAssignedTo(reviewer);
        s.setStatus(ScreenplayStatus.AGUARDANDO_APROVACAO);
        s.setUpdatedAt(Instant.now());
        return s;
    }

    @Transactional
    public Screenplay vote(Long id, Long approverId, VoteRequest req) {
        Screenplay s = get(id);
        User approver = validateRole(approverId, Role.APROVADOR);
        if (!(s.getStatus() == ScreenplayStatus.AGUARDANDO_APROVACAO ||
              s.getStatus() == ScreenplayStatus.EM_APROVACAO)) {
            throw new IllegalStateException("Roteiro não está em aprovação.");
        }

        voteRepo.findByScreenplayAndApprover(s, approver).ifPresent(v -> {
            throw new IllegalStateException("Aprovador já votou.");
        });

        // Registrar voto
        Vote v = Vote.builder()
            .screenplay(s)
            .approver(approver)
            .approved(req.approved())
            .note(req.note())
            .createdAt(Instant.now())
            .build();
        voteRepo.save(v);

        // Atualizar status com base nos votos
        List<Vote> all = voteRepo.findByScreenplay(s);

        // Regras:
        // - Primeiro voto -> EM_APROVACAO
        // - Qualquer "não" -> RECUSADO
        // - 3 "sim" -> APROVADO
        long noCount = all.stream().filter(vt -> !vt.getApproved()).count();
        long yesCount = all.stream().filter(Vote::getApproved).count();

        if (noCount > 0) {
            s.setStatus(ScreenplayStatus.RECUSADO);
        } else if (yesCount >= 3) {
            s.setStatus(ScreenplayStatus.APROVADO);
        } else {
            s.setStatus(ScreenplayStatus.EM_APROVACAO);
        }
        s.setUpdatedAt(Instant.now());
        return s;
    }

    public Screenplay get(Long id) {
        return screenplayRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Roteiro não encontrado"));
    }

    public Screenplay getByPublicToken(String token) {
        return screenplayRepo.findByPublicToken(token).orElseThrow(() -> new EntityNotFoundException("Token inválido"));
    }

    private User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private void ensureStatus(Screenplay s, ScreenplayStatus expected) {
        if (s.getStatus() != expected) throw new IllegalStateException("Fluxo inválido: esperado " + expected);
    }

    private User validateRole(Long userId, Role required) {
        User u = getUser(userId);
        if (required != null && u.getRole() != required) {
            throw new IllegalStateException("Usuário não possui cargo " + required);
        }
        return u;
    }
}