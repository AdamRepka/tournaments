package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.MatchSetDto;
import com.repasoft.tournaments.dto.TournamentDto;
import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.model.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    TournamentDto tournamentToTournamentDto(Tournament tournament);
    Tournament tournamentDtoToTournament(TournamentDto tournamentDto);
}
