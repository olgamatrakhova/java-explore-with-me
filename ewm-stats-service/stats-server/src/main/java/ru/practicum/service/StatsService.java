package ru.practicum.service;

import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface StatsService {
    StatsDto addStat(StatsDto statDto);

    List<StatsResponseDto> getStat(String start, String end, List<String> uris, boolean unique) throws UnsupportedEncodingException;
}