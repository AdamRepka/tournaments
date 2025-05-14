package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.MatchSetDto;
import com.repasoft.tournaments.model.MatchSet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class MatchSetMapper {

    public MatchSetDto toDto(MatchSet matchSet) {
        if (matchSet == null) return null;
        return MatchSetDto.builder()
                .id(matchSet.getId())
                .player1Points(matchSet.getPlayer1Points())
                .player2Points(matchSet.getPlayer2Points())
                .build();
    }
    public MatchSet toMatchSet(MatchSetDto matchSetDto) {
        if (matchSetDto == null) return null;
        return MatchSet.builder()
                .id(matchSetDto.getId())
                .player1Points(matchSetDto.getPlayer1Points())
                .player2Points(matchSetDto.getPlayer2Points())
                .build();
    };
}
