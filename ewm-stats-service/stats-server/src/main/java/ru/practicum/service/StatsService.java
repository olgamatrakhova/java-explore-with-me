package ru.practicum.service;

import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsDto addStat(StatsDto statDto);

    List<StatsResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}