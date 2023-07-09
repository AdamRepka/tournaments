package com.tournaments.tournamentsbe.model.competition;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;
    private List<Player> playerList;
    private List<Match> matchList;
}
