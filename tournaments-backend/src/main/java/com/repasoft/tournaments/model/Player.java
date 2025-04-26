package com.repasoft.tournaments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private PlayerCategory category;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    private String code;

    private boolean entryFeePaid;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerStatistics playerStatistics;
}
