package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<TournamentGroup, Long> {
}
