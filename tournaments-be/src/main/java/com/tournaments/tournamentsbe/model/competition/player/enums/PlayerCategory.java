package com.tournaments.tournamentsbe.model.competition.player.enums;

import jakarta.persistence.Entity;

public enum PlayerCategory {
    SENIOR("SR"),
    JUNIOR("JR"),
    WOMEN("WO");

    private String id;

    PlayerCategory(String id) {
        this.id = id;
    }
}
