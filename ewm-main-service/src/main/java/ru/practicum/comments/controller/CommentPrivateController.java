package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentCreateDto;
import ru.practicum.comments.service.CommentPrivateService;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateController {
    private final CommentPrivateService service;

    @PostMapping("/users/{userId}/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @RequestBody @Validated CommentCreateDto commentCreateDto) {
        log.info("GET request to /users/{userId}/events/{eventId}/comment endpoint");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addComment(userId, eventId, commentCreateDto));
    }

    @DeleteMapping("/users/{userId}/comment/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteComment(@PathVariable Long userId, @PathVariable Long comId) {
        log.info("GET request to /users/{userId}/comment/{comId} endpoint");
        service.deleteComment(userId, comId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Комментарий удален пользователем");
    }

    @PatchMapping("/users/{userId}/comment/{comId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId, @PathVariable Long comId,
                                                    @RequestBody @Validated CommentCreateDto commentCreateDto) {
        log.info("PATCH request to users/{userId}/comment/{comId} endpoint");
        return ResponseEntity.ok(service.updateComment(userId, comId, commentCreateDto));
    }
}