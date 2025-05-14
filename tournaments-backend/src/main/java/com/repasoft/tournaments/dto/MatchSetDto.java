package com.repasoft.tournaments.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchSetDto {
    private Long id;
    private int player1Points;
    private int player2Points;
}
