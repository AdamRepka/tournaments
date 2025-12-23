package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.PlayOffMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayOffMatchRepository extends JpaRepository<PlayOffMatch, Long> {
}
