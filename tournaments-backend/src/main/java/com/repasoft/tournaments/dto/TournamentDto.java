package com.repasoft.tournaments.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentDto {
    private Long id;
    private String name;
    private List<TournamentGroupDto> groups;
}
