package com.tournaments.tournamentsbe.model.competition.player;

import com.tournaments.tournamentsbe.model.competition.player.enums.PlayerCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PLAYER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private boolean deployed;
    @Enumerated(EnumType.STRING)
    private PlayerCategory playerCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerStatistics playerStatistics;
}
