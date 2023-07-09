package com.tournaments.tournamentsbe.service.api;

import com.tournaments.tournamentsbe.model.competition.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerServiceInt {
    List<Player> getAllPlayers();
}
