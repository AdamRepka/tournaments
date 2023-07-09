package com.tournaments.tournamentsbe.model.competition;

import com.tournaments.tournamentsbe.model.competition.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PLAYER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean deployed;
    private Category category;
    private Long groupPosition;
    private Long overallPosition;
    private Long balls;
    private Long sets;
    private Long points;
}
