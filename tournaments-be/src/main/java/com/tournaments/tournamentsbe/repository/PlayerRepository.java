package com.tournaments.tournamentsbe.repository;

import com.tournaments.tournamentsbe.model.competition.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
