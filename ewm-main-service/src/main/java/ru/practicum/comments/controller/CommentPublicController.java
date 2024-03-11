package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/comment/{comId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long comId) {
        log.info("GET request to /comment/{comId}endpoint");
        return ResponseEntity.ok(commentService.getCommentById(comId));
    }

    @GetMapping("/events/{eventId}/comment")
    public ResponseEntity<List<CommentShortDto>> getCommentsByEventId(@PathVariable Long eventId,
                                                                      @RequestParam(defaultValue = "0") int from,
                                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("GET request to /events/{eventId}/comment");
        return ResponseEntity.ok(commentService.getCommentsByEvent(eventId, from, size));
    }
}