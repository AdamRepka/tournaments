package com.tournaments.tournamentsbe.repository;

import com.tournaments.tournamentsbe.model.competition.player.PlayerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerStatisticRepository extends JpaRepository<PlayerStatistics, Integer> {
}
