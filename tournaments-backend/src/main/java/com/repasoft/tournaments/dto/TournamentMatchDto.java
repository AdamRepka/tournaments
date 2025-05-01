package com.repasoft.tournaments.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentMatchDto {
    private Long id;
    private Long playerAId;
    private Long playerBId;
    private Integer setsPlayerA;
    private Integer setsPlayerB;
    private Integer ballsPlayerA;
    private Integer ballsPlayerB;
    private boolean completed;
}
