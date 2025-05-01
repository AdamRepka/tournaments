package com.repasoft.tournaments.dto;

import com.repasoft.tournaments.model.enums.PlayerCategory;
import com.repasoft.tournaments.model.enums.SkillLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private PlayerCategory category;
    private SkillLevel skillLevel;
    private String code;
    private boolean entryFeePaid;

    private Integer points;
    private Integer sets;
    private Integer balls;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer groupPosition;
    private Integer tournamentPosition;
}
