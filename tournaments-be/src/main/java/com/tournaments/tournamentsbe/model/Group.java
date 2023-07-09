package com.tournaments.tournamentsbe.model;

import jakarta.persistence.Entity;

import java.util.List;


public class Group {
    private Long id;
    private String name;
    private List<Player> playerList;
    private List<Match> matches;
}
