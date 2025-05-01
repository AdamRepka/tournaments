package com.repasoft.tournaments.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatisticsDto {
    private Long id;
    private int points;
    private int setsWon;
    private int setsLost;
    private int ballsWon;
    private int ballsLost;
    private int matchesWon;
    private int matchesLost;
    private int matchesDraw;
    private int groupPosition;
    private int tournamentPosition;
    private Long playerId; // referenčně – ne celý objekt Player, jen ID
}
