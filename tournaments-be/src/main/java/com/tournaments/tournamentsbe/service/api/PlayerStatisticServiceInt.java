package com.tournaments.tournamentsbe.service.api;

import com.tournaments.tournamentsbe.model.competition.player.PlayerStatistics;

import java.util.List;

public interface PlayerStatisticServiceInt {
    List<PlayerStatistics> getAllPlayerStatistics();
    void savePlayerStatistic(final PlayerStatistics playerStatistics);
    PlayerStatistics getPlayerStatistics(int id);
}
