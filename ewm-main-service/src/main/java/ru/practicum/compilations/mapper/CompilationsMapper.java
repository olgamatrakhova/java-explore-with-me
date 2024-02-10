package ru.practicum.compilations.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationsMapper {
    public CompilationsDto toCompilationsDtoFromCompilations(Compilations compilations) {
        return CompilationsDto.builder()
                .events(EventMapper.toListEventShortDto(EventMapper.toListEventShort(new ArrayList<>(compilations.getEvents()))))
                .id(compilations.getId())
                .pinned(compilations.getPinned())
                .title(compilations.getTitle())
                .build();
    }

    public static CompilationsDto toCompilationsDto(Compilations compilations) {
        List<EventShortDto> eventShortDtoList = compilations.getEvents().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return CompilationsDto.builder()
                .id(compilations.getId())
                .pinned(compilations.getPinned())
                .title(compilations.getTitle())
                .events(eventShortDtoList)
                .build();
    }

    public static List<CompilationsDto> toCompilationsDtoList(List<Compilations> compilationsList) {
        return compilationsList.stream()
                .map(CompilationsMapper::toCompilationsDto)
                .collect(Collectors.toList());
    }
}