package com.tournaments.tournamentsbe.model.competition;

import java.util.HashMap;
import java.util.Map;

public class MatchSet {
    private final Map<Long, Score> scoreMap = new HashMap<>();

    public Map<Long, Score> getScoreMap() {
        return scoreMap;
    }

    public void addScore(Score score) {
        scoreMap.put(scoreMap.size() + 1L, score);
    }
}
