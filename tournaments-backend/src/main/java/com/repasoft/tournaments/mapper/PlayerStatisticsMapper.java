package com.repasoft.tournaments.mapper;

import com.repasoft.tournaments.dto.PlayerStatisticsDto;
import com.repasoft.tournaments.model.PlayerStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerStatisticsMapper {
    PlayerStatisticsMapper INSTANCE = Mappers.getMapper(PlayerStatisticsMapper.class);

    PlayerStatisticsDto playerStatisticsToPlayerStatisticsDto(PlayerStatistics playerStatistics);
    PlayerStatistics playerStatisticsDtoToPlayerStatistics(PlayerStatisticsDto playerStatisticsDto);
}
