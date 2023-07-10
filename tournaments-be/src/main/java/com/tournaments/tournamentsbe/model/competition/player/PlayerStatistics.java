package com.tournaments.tournamentsbe.model.competition.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class PlayerStatistics {

    @Id
    private int id;
    private int playerId;
    private int points;
    private int sets;
    private int balls;
    private int groupPosition;
    private int overallPosition;
}
