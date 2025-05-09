package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.PlayerStatistics;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player playerDtoToPlayer(PlayerDto dto) {
        if (dto == null) return null;

        PlayerStatistics stats = PlayerStatistics.builder()
                .points(dto.getPoints() != null ? dto.getPoints() : 0)
                .setsWon(dto.getSets() != null ? dto.getSets() : 0)
                .setsLost(0) // defaultně 0, protože DTO nemá setsLost
                .ballsWon(dto.getBalls() != null ? dto.getBalls() : 0)
                .ballsLost(0) // defaultně 0, protože DTO nemá ballsLost
                .matchesWon(dto.getWins() != null ? dto.getWins() : 0)
                .matchesLost(dto.getLosses() != null ? dto.getLosses() : 0)
                .matchesDraw(dto.getDraws() != null ? dto.getDraws() : 0)
                .groupPosition(dto.getGroupPosition() != null ? dto.getGroupPosition() : 0)
                .tournamentPosition(dto.getTournamentPosition() != null ? dto.getTournamentPosition() : 0)
                .build();

        Player player = Player.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .category(dto.getCategory())
                .skillLevel(dto.getSkillLevel())
                .code(dto.getCode())
                .entryFeePaid(dto.isEntryFeePaid())
                .playerStatistics(stats)
                .build();

        stats.setPlayer(player); // nastavíme vztah zpět

        return player;
    }

    public PlayerDto playerToPlayerDto(Player player) {
        if (player == null) return null;

        PlayerStatistics stats = player.getPlayerStatistics();

        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .category(player.getCategory())
                .skillLevel(player.getSkillLevel())
                .code(player.getCode())
                .entryFeePaid(player.isEntryFeePaid())
                .points(stats != null ? stats.getPoints() : null)
                .sets(stats != null ? stats.getSetsWon() : null)  // mapujeme setsWon → sets
                .balls(stats != null ? stats.getBallsWon() : null) // mapujeme ballsWon → balls
                .wins(stats != null ? stats.getMatchesWon() : null)
                .losses(stats != null ? stats.getMatchesLost() : null)
                .draws(stats != null ? stats.getMatchesDraw() : null)
                .groupPosition(stats != null ? stats.getGroupPosition() : null)
                .tournamentPosition(stats != null ? stats.getTournamentPosition() : null)
                .build();
    }
}
