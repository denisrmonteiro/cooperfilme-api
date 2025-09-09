package com.cooperfilme.domain.repository;

import com.cooperfilme.domain.model.Vote;
import com.cooperfilme.domain.model.Screenplay;
import com.cooperfilme.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findByScreenplayAndApprover(Screenplay s, User u);
    List<Vote> findByScreenplay(Screenplay s);
}