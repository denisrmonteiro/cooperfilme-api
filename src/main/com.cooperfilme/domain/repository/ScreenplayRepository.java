package com.cooperfilme.domain.repository;

import com.cooperfilme.domain.model.Screenplay;
import com.cooperfilme.domain.model.ScreenplayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface ScreenplayRepository extends JpaRepository<Screenplay,Long>, JpaSpecificationExecutor<Screenplay> {
    Optional<Screenplay> findByPublicToken(String token);
    long countByIdAndStatus(Long id, ScreenplayStatus status);
}