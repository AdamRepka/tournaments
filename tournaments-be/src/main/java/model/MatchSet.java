package model;

import jakarta.persistence.Entity;

import java.util.HashMap;
import java.util.List;
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
