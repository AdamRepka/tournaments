package com.repasoft.tournaments.dto;

import com.repasoft.tournaments.model.enums.StartRound;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayOffSetupDto {
    private StartRound startRound;
    private int setsToWin;
}
