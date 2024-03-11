package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationsDto;

import java.util.List;

public interface CompilationsPublicService {
    CompilationsDto getCompilationsById(Long compId);

    List<CompilationsDto> getAllCompilations(Boolean pinned, int from, int size);
}