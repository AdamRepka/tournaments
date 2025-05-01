package com.repasoft.tournaments.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentGroupDto {
    private Long id;
    private String name;
    private List<PlayerDto> players;
    private List<TournamentMatchDto> matches;
}
