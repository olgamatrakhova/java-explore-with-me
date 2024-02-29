package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEventsByFilterPublic(String text, List<Long> category, Boolean paid, LocalDateTime start,
                                                LocalDateTime end, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEventByIdPublic(Long id, HttpServletRequest request);
}
