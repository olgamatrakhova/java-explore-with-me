package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.service.CommentPrivateService;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.EventStatus;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.stat.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.practicum.utils.Utils.createPageRequestDesc;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final StatsService statsService;

    private final CommentPrivateService commentPrivateService;

    @Override
    public List<EventShortDto> getEventsByFilterPublic(String text, List<Long> category, Boolean paid, LocalDateTime start,
                                                       LocalDateTime end, Boolean onlyAvailable, String sort, Integer from,
                                                       Integer size, HttpServletRequest request) {
        log.info("Вызов публичного сервиса получения данных по событию с параметрами из фильтра getEventsByFilterPublic({},{},{},{},{},{},{},{},{},{})", text, category, paid, start, end, onlyAvailable, sort, from, size, request);
        sort = (sort != null && sort.equals("EVENT_DATE")) ? "eventDate" : "id";
        if (start != null || end != null) {
            if (end.isBefore(start)) {
                log.error("Дата и время окончания события не могут быть раньше, чем дата и время начала события");
                throw new BadRequestException("Дата и время окончания события не могут быть раньше, чем дата и время начала события");
            }
        }
        List<Event> list = eventRepository.findAllEvents(text, category, paid, start, end,
                onlyAvailable, sort, createPageRequestDesc(sort, from, size));
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(list);
        Map<Long, Long> view = statsService.toView(list);
        Map<Long, Long> commentCount = commentPrivateService.getCommentCount(list);
        List<EventDto> events = new ArrayList<>();
        list.forEach(event -> events.add(EventMapper.toEventShort(event, view.getOrDefault(event.getId(), 0L),
                confirmedRequest.getOrDefault(event.getId(), 0L), commentCount.getOrDefault(event.getId(), 0L))));
        statsService.addHits(request);
        log.info("Возвращено {} событий", events.size());
        return EventMapper.toListEventShortDto(events);
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        log.info("Вызов публичного сервиса получение информации по событию getEventByIdPublic({},{})", id, request);
        Event event = eventRepository.findAllEventsById(id).orElseThrow(() -> {
            log.error("События с id = {} не существует", id);
            return new NotFoundException("События не существует");
        });
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("События с id = {} не опубликовано", id);
            throw new NotFoundException("Событие не опубликовано");
        }
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statsService.toView(List.of(event));
        statsService.addHits(request);
        event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
        event.setView(view.getOrDefault(event.getId(), 0L));
        log.info("Возвращено событие - {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }
}