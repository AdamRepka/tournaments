package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.enums.PlayerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByCategory(final PlayerCategory category);
}
