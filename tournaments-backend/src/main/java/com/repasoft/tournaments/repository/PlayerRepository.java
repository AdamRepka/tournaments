package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
