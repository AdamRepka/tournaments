package com.tournaments.tournamentsbe.service.api;

import com.tournaments.tournamentsbe.model.competition.player.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerServiceInt {
    List<Player> getAllPlayers();
    void savePlayer(final Player player);
}
