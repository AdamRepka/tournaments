package com.tournaments.tournamentsbe.service.controller;

import com.tournaments.tournamentsbe.model.competition.player.PlayerStatistics;
import com.tournaments.tournamentsbe.model.competition.player.enums.PlayerCategory;
import com.tournaments.tournamentsbe.service.impl.PlayerServiceImpl;
import com.tournaments.tournamentsbe.model.competition.player.Player;
import com.tournaments.tournamentsbe.service.impl.PlayerStatisticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private PlayerStatisticServiceImpl playerStatisticService;

    @GetMapping("/players")
    public List<Player> getAll() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.setPlayerId(1);
        playerStatistics.setSets(0);
        playerStatistics.setPoints(0);
        playerStatistics.setBalls(0);
        playerStatistics.setGroupPosition(0);
        playerStatistics.setOverallPosition(0);
        playerStatisticService.savePlayerStatistic(playerStatistics);
        Player player = new Player();
        player.setFirstName("Test");
        player.setLastName("Name");
        player.setPlayerCategory(PlayerCategory.SENIOR);
        player.setPlayerStatistics(playerStatisticService.getPlayerStatistics(1));
        player.setDeployed(false);
        playerService.savePlayer(player);
        return playerService.getAllPlayers();
    }
}
