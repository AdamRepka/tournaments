package com.repasoft.tournaments.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentGroupDto {
    private Long id;
    private String name;

    @Builder.Default
    private List<PlayerDto> players = new ArrayList<>();

    @Builder.Default
    private List<TournamentMatchDto> matches = new ArrayList<>();
}
