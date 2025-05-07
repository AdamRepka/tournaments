package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<TournamentGroup, Long> {
    @Query("SELECT DISTINCT g FROM TournamentGroup g LEFT JOIN FETCH g.players LEFT JOIN FETCH g.matches")
    List<TournamentGroup> findAllWithPlayersAndMatches();
}
