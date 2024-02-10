package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.NewCompilationsDto;

public interface CompilationsAdminService {
    CompilationsDto addCompilations(NewCompilationsDto newCompilationsDto);

    void deleteCompilations(Long compId);

    CompilationsDto updateCompilations(Long compId, NewCompilationsDto newCompilationsDto);
}