package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.NewCompilationsDto;
import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotUniqueException;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompilationsAdminServiceImpl implements CompilationsAdminService {
    private final CompilationsRepository compilationsRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationsDto addCompilations(NewCompilationsDto newCompilationsDto) {
        log.info("Вызов добавление подборок событий addCompilations({})", newCompilationsDto);
        if (newCompilationsDto.getTitle() == null) {
            throw new BadRequestException("Не заполнен заголовок подборки");
        }
        if (newCompilationsDto.getTitle().trim().equals("")) {
            throw new BadRequestException("Заголовок подборки не может быть пустым");
        }
        if (compilationsRepository.existsByTitle(newCompilationsDto.getTitle())) {
            throw new NotUniqueException("Подборка с таким названием уже существует");
        }
        Set<Event> eventSet;
        eventSet = (newCompilationsDto.getEvents() != null && newCompilationsDto.getEvents().size() != 0) ?
                new HashSet<>(eventRepository.findAllById(newCompilationsDto.getEvents())) : new HashSet<>();
        Compilations compilations = Compilations.builder()
                .pinned(newCompilationsDto.getPinned() != null && newCompilationsDto.getPinned())
                .title(newCompilationsDto.getTitle())
                .events(eventSet)
                .build();
        return CompilationsMapper.toCompilationsDtoFromCompilations(compilationsRepository.save(compilations));
    }

    @Override
    public void deleteCompilations(Long compId) {
        log.info("Вызов удаления подборки deleteCompilations({})", compId);
        if (!compilationsRepository.existsById(compId)) {
            throw new NotFoundException("Подборка не существует");
        }
        log.info("Подборка {} удалена", compId);
        compilationsRepository.deleteById(compId);
    }

    @Override
    public CompilationsDto updateCompilations(Long compId, NewCompilationsDto newCompilationsDto) {
        log.info("Вызов обновления подборки updateCompilations({},{})", compId, newCompilationsDto);
        Compilations compilations = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки с id " + compId + " не существует"));
        if (newCompilationsDto.getTitle() != null) {
            compilations.setTitle(newCompilationsDto.getTitle());
        }
        if (newCompilationsDto.getPinned() != null) {
            compilations.setPinned(newCompilationsDto.getPinned());
        }
        if (newCompilationsDto.getEvents() != null) {
            HashSet<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationsDto.getEvents()));
            compilations.setEvents(events);
        }
        Compilations updatedCompilations = compilationsRepository.save(compilations);
        log.info("Подборка {} обновлена", compId);
        return CompilationsMapper.toCompilationsDtoFromCompilations(updatedCompilations);
    }
}