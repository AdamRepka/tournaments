package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.dto.TournamentMatchDto;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.PlayerStatistics;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.repository.GroupRepository;
import com.repasoft.tournaments.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TournamentMatchMapper {

    private final PlayerRepository playerRepository;
    private final MatchSetMapper matchSetMapper;
    private final GroupRepository groupRepository;

    public TournamentMatch toMatch(TournamentMatchDto dto) {
        if (dto == null) return null;
        return TournamentMatch.builder()
                .id(dto.getId())
                .player1(playerRepository.getReferenceById(dto.getPlayer1Id()))
                .player2(playerRepository.getReferenceById(dto.getPlayer2Id()))
                .matchSets(dto.getMatchSets().stream()
                        .map(matchSetMapper::toMatchSet)
                        .toList())
                .tournamentGroup(groupRepository.findById(dto.getTournamentGroupId()).orElse(null))
                .build();
    }

    public TournamentMatchDto toDto(TournamentMatch tournamentMatch) {
        if (tournamentMatch == null) return null;
        return TournamentMatchDto.builder()
                .id(tournamentMatch.getId())
                .tournamentGroupId(tournamentMatch.getTournamentGroup().getId())
                .player1Id(tournamentMatch.getPlayer1().getId())
                .player2Id(tournamentMatch.getPlayer2().getId())
                .matchSets(tournamentMatch.getMatchSets().stream().map(matchSetMapper::toDto).toList())
                .build();
    }
}
