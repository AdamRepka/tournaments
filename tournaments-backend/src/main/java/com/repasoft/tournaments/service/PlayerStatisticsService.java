package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.PlayerStatistics;
import com.repasoft.tournaments.repository.PlayerStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerStatisticsService {

    private final PlayerStatisticsRepository playerStatisticsRepository;

    @Autowired
    public PlayerStatisticsService(PlayerStatisticsRepository playerStatisticsRepository) {
        this.playerStatisticsRepository = playerStatisticsRepository;
    }

    public List<PlayerStatistics> getAllStatistics() {
        return playerStatisticsRepository.findAll();
    }

    public Optional<PlayerStatistics> getStatisticsById(Long id) {
        return playerStatisticsRepository.findById(id);
    }

    public PlayerStatistics createStatistics(PlayerStatistics statistics) {
        return playerStatisticsRepository.save(statistics);
    }

    public PlayerStatistics updateStatistics(Long id, PlayerStatistics updatedStatistics) {
        return playerStatisticsRepository.findById(id)
                .map(statistics -> {
                    statistics.setGroupPosition(updatedStatistics.getGroupPosition());
                    statistics.setTournamentPosition(updatedStatistics.getTournamentPosition());
                    statistics.setPoints(updatedStatistics.getPoints());
                    statistics.setSetsWon(updatedStatistics.getSetsWon());
                    statistics.setSetsLost(updatedStatistics.getSetsLost());
                    statistics.setBallsWon(updatedStatistics.getBallsWon());
                    statistics.setBallsLost(updatedStatistics.getBallsLost());
                    statistics.setWins(updatedStatistics.getWins());
                    statistics.setLosses(updatedStatistics.getLosses());
                    statistics.setDraws(updatedStatistics.getDraws());
                    return playerStatisticsRepository.save(statistics);
                })
                .orElseThrow(() -> new RuntimeException("Statistics not found with id " + id));
    }

    public void deleteStatistics(Long id) {
        playerStatisticsRepository.deleteById(id);
    }
}
