package com.tournaments.tournamentsbe.model.competition.player.enums;

import jakarta.persistence.Entity;

public enum PlayerCategory {
    SR("SR"),
    JR("JR"),
    WO("WO");

    private final String id;

    PlayerCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

