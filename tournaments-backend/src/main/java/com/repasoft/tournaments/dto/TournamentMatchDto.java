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
    private Long playerHomeId;
    private Long playerAwayId;
    private List<MatchSetDto> matchSets;
    private Long tournamentGroupId;
    private int playerHomeSetsWon;
    private int playerAwaySetsWon;
}
