package com.repasoft.tournaments.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentMatchDto {
    private Long id;
    private Long player1Id;
    private Long player2Id;
    private List<MatchSetDto> matchSets;
    private Long tournamentGroupId;
    private int player1SetsWon;
    private int player2SetsWon;
}
