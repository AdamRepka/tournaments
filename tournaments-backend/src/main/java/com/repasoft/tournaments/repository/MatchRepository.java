package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<TournamentMatch, Long> {
}
