package com.repasoft.tournaments.model.print;

import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.TournamentGroup;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchPaper {

    private final Player firstPlayer;
    private final Player secondPlayer;
    private final TournamentGroup group;
    private final int numberOfSets;
}
