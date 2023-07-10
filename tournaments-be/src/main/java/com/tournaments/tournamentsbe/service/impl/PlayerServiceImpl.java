package com.tournaments.tournamentsbe.service.impl;

import com.tournaments.tournamentsbe.model.competition.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.tournaments.tournamentsbe.repository.PlayerRepository;
import com.tournaments.tournamentsbe.service.api.PlayerServiceInt;

import java.util.List;

@Service
@Component
public class PlayerServiceImpl implements PlayerServiceInt {

    @Autowired
    private PlayerRepository playerRepository;
    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public void savePlayer(final Player player) {
        playerRepository.save(player);
    }
}
