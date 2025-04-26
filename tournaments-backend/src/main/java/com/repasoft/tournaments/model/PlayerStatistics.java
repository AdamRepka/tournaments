package com.repasoft.tournaments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;

    private int setsWon;

    private int setsLost;

    private int ballsWon;

    private int ballsLost;

    private int matchesWon;

    private int matchesLost;

    private int matchesDraw;

    private int groupPosition;

    private int tournamentPosition;

    @OneToOne
    private Player player;
}
