package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.PlayerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Long> {
    Optional<PlayerStatistics> findByPlayer(Player player);
}
