package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.service.EventPrivateService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestShortUpdateDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
@Slf4j
public class EventPrivateController {
    private final EventPrivateService eventPrivateService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId,
                                                 @RequestBody @Validated EventRequestDto eventRequestDto) {
        log.info("POST request to /users/{userId}/events");
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPrivateService.addEvent(userId, eventRequestDto));
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventFullDto>> getEventByUserId(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to /users/{userId}/events");
        return ResponseEntity.ok(eventPrivateService.getEventByUserId(userId, from, size));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                    @RequestBody @Validated EventUpdateDto eventUpdateDto) {
        log.info("PATCH request to users/{userId}/events/{eventId}");
        return ResponseEntity.ok(eventPrivateService.updateEvent(userId, eventId, eventUpdateDto));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET request to users/{userId}/events/{eventId}");
        return ResponseEntity.ok(eventPrivateService.getEventByUserIdAndEventId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestShortUpdateDto> patchRequestByOwnerUser(@PathVariable Long userId, @PathVariable Long eventId,
                                                                         @RequestBody RequestShortDto requestShortDto) {
        log.info("PATCH request to users/{userId}/events/{eventId}/requests");
        return ResponseEntity.ok(eventPrivateService.updateRequestByOwner(userId, eventId, requestShortDto));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequestByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("PATCH request to users/{userId}/events/{eventId}");
        return ResponseEntity.ok(eventPrivateService.getRequestByUserIdAndEventId(userId, eventId));
    }
}