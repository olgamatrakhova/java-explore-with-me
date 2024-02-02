package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    @Transactional
    StatsDto addStat(StatsDto statDto);

    @Transactional
    List<StatsResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}