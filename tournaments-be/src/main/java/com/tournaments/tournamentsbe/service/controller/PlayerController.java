package com.tournaments.tournamentsbe.service.controller;

import com.tournaments.tournamentsbe.service.impl.PlayerServiceImpl;
import com.tournaments.tournamentsbe.model.Player;
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
