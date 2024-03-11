package ru.practicum.stat.service;

import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

public interface StatsService {
    Map<Long, Long> toConfirmedRequest(Collection<Event> list);

    Map<Long, Long> toView(Collection<Event> list);

    void addHits(HttpServletRequest request);
}