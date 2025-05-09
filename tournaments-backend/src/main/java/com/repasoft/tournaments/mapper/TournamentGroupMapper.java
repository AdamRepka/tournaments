package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TournamentGroupMapper {

    private final PlayerMapper playerMapper;

    public TournamentGroupDto tournamentGroupToTournamentGroupDto(TournamentGroup tournamentGroup) {
        if (tournamentGroup == null) return null;

        return TournamentGroupDto.builder()
                .id(tournamentGroup.getId())
                .name(tournamentGroup.getName())
                .players(
                        tournamentGroup.getPlayers() != null ?
                                tournamentGroup.getPlayers().stream()
                                        .map(playerMapper::playerToPlayerDto)
                                        .collect(Collectors.toList())
                                : null
                )
                .matches(null) // TODO: přidej mapování pro TournamentMatch pokud potřebuješ
                .build();
    }

    public TournamentGroup tournamentGroupDtoToTournamentGroup(TournamentGroupDto tournamentGroupDto) {
        if (tournamentGroupDto == null) return null;

        return TournamentGroup.builder()
                .id(tournamentGroupDto.getId())
                .name(tournamentGroupDto.getName())
                .players(
                        tournamentGroupDto.getPlayers() != null ?
                                tournamentGroupDto.getPlayers().stream()
                                        .map(playerMapper::playerDtoToPlayer)
                                        .collect(Collectors.toList())
                                : null
                )
                .matches(null) // TODO: přidej mapování pro TournamentMatch pokud potřebuješ
                .build();
    }
}
