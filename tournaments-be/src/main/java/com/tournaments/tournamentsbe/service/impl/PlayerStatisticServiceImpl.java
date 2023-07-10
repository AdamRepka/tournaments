package com.tournaments.tournamentsbe.service.impl;

import com.tournaments.tournamentsbe.model.competition.player.PlayerStatistics;
import com.tournaments.tournamentsbe.repository.PlayerStatisticRepository;
import com.tournaments.tournamentsbe.service.api.PlayerStatisticServiceInt;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerStatisticServiceImpl implements PlayerStatisticServiceInt {

    @Autowired
    private PlayerStatisticRepository playerStatisticRepository;

    @Override
    public List<PlayerStatistics> getAllPlayerStatistics() {
        return playerStatisticRepository.findAll();
    }

    @Override
    public void savePlayerStatistic(PlayerStatistics playerStatistics) {
        playerStatisticRepository.save(playerStatistics);
    }

    @Override
    public PlayerStatistics getPlayerStatistics(int id) {
        return playerStatisticRepository.getReferenceById(id);
    }
}
