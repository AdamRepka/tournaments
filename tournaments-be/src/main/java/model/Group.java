package model;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Group {
    private Long id;
    private String name;
    private List<Player> playerList;
    private List<Match> matches;
}
