package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.event.dto.EventAdminDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestAdminDto;
import ru.practicum.event.location.repository.LocationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.EventStatus;
import ru.practicum.event.status.EventStatusAdmin;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.stat.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.utils.Utils.createPageRequestAsc;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final StatsService statsService;

    @Override
    public EventFullDto updateEventAdmin(Long eventId, EventRequestAdminDto eventRequestAdminDto) {
        log.info("Вызов обновления события updateEventAdmin({},{})", eventId, eventRequestAdminDto);
        EventAdminDto eventAdminDto = EventMapper.toEventAdminFromAdminDto(eventRequestAdminDto);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id = " + eventId + " не найдено "));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("Событие {} не возможно изменить, оно уже началось или уже закончилось", event.getTitle());
            throw new BadRequestException("Событие " + event.getTitle() + " не возможно изменить, оно уже началось или уже закончилось");
        }
        if (eventAdminDto.getEventDate() != null) {
            if (eventAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                log.error("Не корректная дата для обновления - {}", eventAdminDto.getEventDate());
                throw new BadRequestException("Не корректная дата для обновления");
            } else {
                event.setEventDate(eventAdminDto.getEventDate());
            }
        }
        if (eventAdminDto.getStateAction() != null) {
            if (!event.getEventStatus().equals(EventStatus.PENDING)) {
                log.error("Нельзя изменить событие, его статус не в ожидании - статус {} ", eventAdminDto.getStateAction());
                throw new ConflictException("Нельзя изменить событие, его статус не в ожидании");
            }
            if (eventAdminDto.getStateAction().equals(EventStatusAdmin.PUBLISH_EVENT)) {
                event.setEventStatus(EventStatus.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().withNano(0));
            }
            if (eventAdminDto.getStateAction().equals(EventStatusAdmin.REJECT_EVENT)) {
                event.setEventStatus(EventStatus.CANCELED);
            }
        }
        if (eventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminDto.getRequestModeration());
        }
        if (eventAdminDto.getPaid() != null) {
            event.setPaid(eventAdminDto.getPaid());
        }
        if (eventAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminDto.getParticipantLimit());
        }
        if (eventAdminDto.getLocation() != null) {
            event.setLocation(locationRepository.findByLatAndLon(eventAdminDto.getLocation().getLat(), eventAdminDto.getLocation().getLon())
                    .orElse(locationRepository.save(eventAdminDto.getLocation())));
        }
        if (eventAdminDto.getAnnotation() != null && !eventAdminDto.getTitle().isBlank()) {
            event.setAnnotation(eventAdminDto.getAnnotation());
        }
        if (eventAdminDto.getDescription() != null && !eventAdminDto.getDescription().isBlank()) {
            event.setDescription(eventAdminDto.getDescription());
        }
        if (eventAdminDto.getTitle() != null && !eventAdminDto.getTitle().isBlank()) {
            event.setTitle(eventAdminDto.getTitle());
        }
        if (eventAdminDto.getCategories() != null) {
            event.setCategories(categoriesRepository.findById(eventAdminDto.getCategories())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statsService.toView(List.of(event));
        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        log.info("Событие {} обновлено", event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsAdmin(List<Long> users, List<EventStatus> eventStatusList,
                                             List<Long> categories, LocalDateTime start, LocalDateTime end,
                                             int from, int size) {
        log.info("Вызов получения события getEventsAdmin({},{},{},{},{},{},{})", users, eventStatusList, categories,
                start, end, from, size);
        List<Event> events = eventRepository.findAllEventsByParam(users, eventStatusList, categories, start, end, createPageRequestAsc(from, size));
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(events);
        Map<Long, Long> view = statsService.toView(events);
        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(view.getOrDefault(event.getId(), 0L));
        }
        log.info("Возвращен список событий - size = {}", events.size());
        return EventMapper.toListEventFullDto(events);
    }
}