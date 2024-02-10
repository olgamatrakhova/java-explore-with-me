package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationsPublicServiceImpl implements CompilationsPublicService {
    private final CompilationsRepository compilationsRepository;

    @Override
    public CompilationsDto getCompilationsById(Long compId) {
        log.info("Вызов получение подборки по идентификатору getCompilationsById({}))", compId);
        Compilations compilations = compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборки не существует"));
        log.info("Получена подборка {}", compilations);
        return CompilationsMapper.toCompilationsDto(compilations);
    }

    @Override
    public List<CompilationsDto> getAllCompilations(Boolean pinned, int from, int size) {
        log.info("Вызов получение всех подборок getAllCompilations({},{},{}))", pinned, from, size);
        Pageable pageable = PageRequest.of(from, size, Sort.Direction.ASC, "id");
        List<Compilations> compilationsList;
        compilationsList = (pinned == null) ? compilationsRepository.findAll(pageable).getContent() :
                compilationsRepository.findAllByPinned(pinned, pageable);
        log.info("Получен список подборок : {}", compilationsList);
        return CompilationsMapper.toCompilationsDtoList(compilationsList);
    }
}