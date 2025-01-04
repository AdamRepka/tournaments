package com.tournaments.tournamentsbe.model.competition.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "player_statistics", schema = "tournaments")
public class PlayerStatistics {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // Pocet bodu
    private int points;
    // Pocet setu
    private int sets;
    // Pocet micku
    private int balls;
    // Pozice ve skupine
    private int groupPosition;
    // Celkova pozice
    private int overallPosition;
    // Vazba na hrace
    @OneToOne
    @JoinColumn(name = "player_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    private Player player;

    @Override
    public String toString() {
        return "PlayerStatistics{" +
                "id=" + id +
                ", points=" + points +
                ", sets=" + sets +
                ", balls=" + balls +
                ", groupPosition=" + groupPosition +
                ", overallPosition=" + overallPosition +
                ", player=" + player +
                '}';
    }
}
