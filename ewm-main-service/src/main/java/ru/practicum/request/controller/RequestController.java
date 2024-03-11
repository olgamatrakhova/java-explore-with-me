package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RequestDto> addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("POST request to /users/{userId}/requests endpoint");
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.addRequest(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequests(@PathVariable Long userId) {
        log.info("GET request to /users/{userId}/requests endpoint");
        return ResponseEntity.ok(requestService.getAllRequests(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> canselRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("PATCH request to /users/{userId}/requests/{requestId}/cancel");
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }
}