package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestShortUpdateDto;

import java.util.List;

public interface EventPrivateService {
    EventFullDto addEvent(Long userId, EventRequestDto eventRequestDto);

    List<EventFullDto> getEventByUserId(Long userId, int from, int size);

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    RequestShortUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto);
}