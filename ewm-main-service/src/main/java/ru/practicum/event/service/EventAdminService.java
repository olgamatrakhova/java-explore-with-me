package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestAdminDto;
import ru.practicum.event.status.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    EventFullDto updateEventAdmin(Long eventId, EventRequestAdminDto eventRequestAdminDto);

    List<EventFullDto> getEventsAdmin(List<Long> users, List<EventStatus> eventStatusList, List<Long> category,
                                      LocalDateTime start, LocalDateTime end, int from, int size);
}