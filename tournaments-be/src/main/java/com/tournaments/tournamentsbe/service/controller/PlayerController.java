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

    @GetMapping("/players")
    public List<Player> getAll() {
        return playerService.getAllPlayers();
    }
}
