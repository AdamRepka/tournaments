package com.repasoft.tournaments.dto;

import com.repasoft.tournaments.model.enums.PlayerCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupGenerationRequestDto {
    private PlayerCategory category;  // SENIOR, JUNIOR, WOMEN
    private int groupCount;
    private int playersPerGroup;
}
