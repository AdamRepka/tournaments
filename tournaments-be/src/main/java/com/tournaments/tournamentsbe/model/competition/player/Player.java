package com.tournaments.tournamentsbe.model.competition.player;

import com.tournaments.tournamentsbe.model.competition.player.enums.PlayerCategory;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player", schema = "tournaments")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "deployed")
    private boolean deployed;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_category_id", length = 2) // Matches DB column definition
    private PlayerCategory playerCategory;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlayerStatistics playerStatistics;

    // Getters and Setters

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", deployed=" + deployed +
                ", playerCategory=" + playerCategory +
                ", playerStatistics=" + playerStatistics +
                '}';
    }
}

