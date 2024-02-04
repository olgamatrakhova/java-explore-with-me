package ru.practicum.mapper;

import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;

public class StatsMapper {
    private StatsMapper() {
    }

    public static Stats toStats(StatsDto statsDto) {
        return Stats.builder()
                .ip(statsDto.getIp())
                .uri(statsDto.getUri())
                .timestamp(statsDto.getTimestamp())
                .app(statsDto.getApp())
                .build();
    }

    public static StatsDto toStatsDto(Stats stat) {
        return StatsDto.builder()
                .timestamp(stat.getTimestamp())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .build();
    }
}