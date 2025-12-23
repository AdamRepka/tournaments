package com.repasoft.tournaments.model.enums;

public enum StartRound {

    SIXTEENTH_FINAL(16),
    EIGHTH_FINAL(8),
    QUARTER_FINAL(4),
    SEMI_FINAL(2);

    private final int matchCount;

    StartRound(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public int getRequiredPlayers() {
        return matchCount * 2;
    }
}
