package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Utils.TIME_STRING;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/events")
@Slf4j
public class EventPublicController {
    private final EventPublicService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByFilterPublic(@RequestParam(required = false) String text,
                                                                       @RequestParam(required = false) List<@Positive Long> categories,
                                                                       @RequestParam(required = false) Boolean paid,
                                                                       @RequestParam(required = false)
                                                                       @DateTimeFormat(pattern = TIME_STRING) LocalDateTime start,
                                                                       @RequestParam(required = false)
                                                                       @DateTimeFormat(pattern = TIME_STRING) LocalDateTime end,
                                                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                       @RequestParam(required = false) String sort,
                                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                       @RequestParam(defaultValue = "10") @Positive int size,
                                                                       HttpServletRequest request) {
        log.info("PATCH request to users/{userId}/events/{eventId}/requests");
        return ResponseEntity.ok(service.getEventsByFilterPublic(text, categories, paid,
                start, end, onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventByIdPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("PATCH request to /events/{id}");
        return ResponseEntity.ok(service.getEventByIdPublic(id, request));
    }
}