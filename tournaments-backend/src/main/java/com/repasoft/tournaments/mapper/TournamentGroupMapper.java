package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.model.TournamentGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TournamentGroupMapper {
    TournamentGroupMapper INSTANCE = Mappers.getMapper(TournamentGroupMapper.class);

    TournamentGroupDto tournamentGroupToTournamentGroupDto(TournamentGroup tournamentGroup);
    TournamentGroup tournamentGroupDtoToTournamentGroup(TournamentGroupDto tournamentGroupDto);
}
