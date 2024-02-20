package ru.practicum.stat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.exception.StatsParseException;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.utils.Utils.FORMATTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatServiceImpl implements StatsService {
    private final RequestRepository requestRepository;

    private final StatsClient statsClient;

    private final ObjectMapper objectMapper;

    @Value("${main_app}")
    private String app;

    @Override
    public Map<Long, Long> toConfirmedRequest(Collection<Event> list) {
        log.info("Вызов подтверждения регистрации toConfirmedRequest({})", list);
        List<Long> listEventId = list.stream().map(Event::getId).collect(Collectors.toList());
        List<ConfirmedRequestShortDto> confirmedRequestShortDtoList = requestRepository.countByEventId(listEventId);
        Map<Long, Long> confirmedRequest = confirmedRequestShortDtoList.stream()
                .collect(Collectors.toMap(ConfirmedRequestShortDto::getEventId, ConfirmedRequestShortDto::getConfirmedRequestsCount));
        log.info("Количество подтвержденных записей {}", confirmedRequest.size());
        return confirmedRequest;
    }

    @Override
    public Map<Long, Long> toView(Collection<Event> events) {
        log.info("Вызов статистики просмотров событий toView({})", events);
        Map<Long, Long> view = new HashMap<>();
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo).orElse(null);
        if (start == null) {
            return Map.of();
        }
        List<String> uris = events.stream().map(a -> "/events/" + a.getId()).collect(Collectors.toList());
        ResponseEntity<Object> response = statsClient.getStatsEvent(start.format(FORMATTER),
                LocalDateTime.now().format(FORMATTER), uris, true);
        try {
            StatsResponseDto[] stats = objectMapper.readValue(objectMapper.writeValueAsString(response.getBody()), StatsResponseDto[].class);
            for (StatsResponseDto stat : stats) {
                view.put(
                        Long.parseLong(stat.getUri()),
                        stat.getHits());
            }
        } catch (JsonProcessingException e) {
            log.error("Не возможно получить ответ ({})", e.getMessage());
            throw new StatsParseException("Не возможно получить статистику по событию");
        }
        log.info("Получено {} просмотров", view.size());
        return view;
    }

    @Override
    public void addHits(HttpServletRequest request) {
        statsClient.addStatsEvent(StatsDto.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .app(app)
                .build());
        log.info("Добавление популярного события");
    }
}