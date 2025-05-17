package com.repasoft.tournaments.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Tournament tournament;

    @OneToMany(mappedBy = "tournamentGroup", cascade = CascadeType.PERSIST)
    private Set<Player> players;

    @OneToMany(mappedBy = "tournamentGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TournamentMatch> matches;
}
