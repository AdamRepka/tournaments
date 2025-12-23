package com.repasoft.tournaments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayOffMatchSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int player1Points;

    private int player2Points;

    @ManyToOne
    private PlayOffMatch match;
}
