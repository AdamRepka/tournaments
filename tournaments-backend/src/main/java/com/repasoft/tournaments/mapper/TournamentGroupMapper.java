package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.MatchSetDto;
import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.dto.TournamentMatchDto;
import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.repository.GroupRepository;
import com.repasoft.tournaments.repository.MatchSetRepository;
import com.repasoft.tournaments.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TournamentGroupMapper {

    private final PlayerMapper playerMapper;
    private final GroupRepository groupRepository;
    private final MatchSetRepository matchSetRepository;
    private final PlayerRepository playerRepository;
    private final MatchSetMapper matchSetMapper;

    public TournamentGroupDto tournamentGroupToTournamentGroupDto(TournamentGroup tournamentGroup) {
        if (tournamentGroup == null) return null;

        return TournamentGroupDto.builder()
                .id(tournamentGroup.getId())
                .name(tournamentGroup.getName())
                .players(
                        tournamentGroup.getPlayers() != null ?
                                tournamentGroup.getPlayers().stream()
                                        .map(playerMapper::playerToPlayerDto)
                                        .sorted(Comparator.comparingInt(PlayerDto::getGroupIndex))
                                        .collect(Collectors.toList())
                                : null
                )
                .matches(tournamentGroup.getMatches().stream()
                        .map(match -> TournamentMatchDto.builder()
                                .id(match.getId())
                                .tournamentGroupId(match.getTournamentGroup().getId())
                                .player1Id(match.getPlayer1().getId())
                                .player2Id(match.getPlayer2().getId())
                                .matchSets(match.getMatchSets().stream()
                                        .map(matchSetMapper::toDto)
                                        .toList())
                                .player1SetsWon(match.getPlayer1SetsWon())
                                .player2SetsWon(match.getPlayer2SetsWon())
                                .build())
                        .toList())// TODO: přidej mapování pro TournamentMatch pokud potřebuješ
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
                                        .collect(Collectors.toSet())
                                : null
                )
                .matches(tournamentGroupDto.getMatches().stream()
                        .map(match -> TournamentMatch.builder()
                                .id(match.getId())
                                .tournamentGroup(groupRepository.findById(match.getTournamentGroupId()).orElse(null))
                                .player1(playerRepository.findById(match.getPlayer1Id()).orElse(null))
                                .player2(playerRepository.findById(match.getPlayer2Id()).orElse(null))
                                .matchSets(match.getMatchSets().stream()
                                        .map(matchSetMapper::toMatchSet)
                                        .toList())
                                .build())
                        .collect(Collectors.toSet())) // TODO: přidej mapování pro TournamentMatch pokud potřebuješ
                .build();
    }
}
