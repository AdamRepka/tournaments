package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.MatchSetDto;
import com.repasoft.tournaments.model.MatchSet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchSetMapper {
    MatchSetMapper INSTANCE = Mappers.getMapper(MatchSetMapper.class);

    MatchSetDto matchSetToMatchSetDto(MatchSet matchSet);
    MatchSet matchSetDtoToMatchSet(MatchSetDto matchSetDto);
}
