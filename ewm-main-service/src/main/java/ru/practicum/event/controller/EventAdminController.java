package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestAdminDto;
import ru.practicum.event.service.EventAdminService;
import ru.practicum.event.status.EventStatus;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Utils.TIME_STRING;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events")
@Slf4j
public class EventAdminController {
    private final EventAdminService eventAdminService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateAdminEvent(@PathVariable Long eventId, @RequestBody @Validated EventRequestAdminDto eventRequestAdminDto) {
        log.info("PATCH request to /admin/events/{eventId}");
        return ResponseEntity.ok(eventAdminService.updateEventAdmin(eventId, eventRequestAdminDto));
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                             @RequestParam(required = false) List<EventStatus> eventStatusList,
                                                             @RequestParam(required = false) List<Long> categories,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = TIME_STRING) LocalDateTime start,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = TIME_STRING) LocalDateTime end,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to /admin/events endpoint");
        return ResponseEntity.ok(eventAdminService.getEventsAdmin(users, eventStatusList, categories, start, end, from, size));
    }
}