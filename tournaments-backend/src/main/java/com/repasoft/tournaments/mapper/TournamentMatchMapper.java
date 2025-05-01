package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.TournamentMatchDto;
import com.repasoft.tournaments.model.TournamentMatch;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TournamentMatchMapper {
    TournamentMatchMapper INSTANCE = Mappers.getMapper(TournamentMatchMapper.class);

    TournamentMatchDto tournamentMatchToTournamentMatchDto(TournamentMatch tournamentMatch);
    TournamentMatch tournamentMatchDtoToTournamentMatch(TournamentMatchDto tournamentMatchDto);
}
