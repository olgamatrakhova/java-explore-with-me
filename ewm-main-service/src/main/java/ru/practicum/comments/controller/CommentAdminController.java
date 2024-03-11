package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentAdminService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentAdminService commentAdminService;

    @GetMapping("comment/search")
    public ResponseEntity<List<CommentDto>> searchComment(@RequestParam String text) {
        log.info("GET request to /admin/comment/search endpoint");
        return ResponseEntity.ok(commentAdminService.searchComment(text));
    }

    @GetMapping("users/{userId}/comment")
    public ResponseEntity<List<CommentDto>> getCommentByUserId(@PathVariable Long userId) {
        log.info("GET request to admin/users/{userId}/comment endpoint");
        return ResponseEntity.ok(commentAdminService.findCommentByUserId(userId));
    }

    @DeleteMapping("comment/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteComment(@PathVariable Long comId) {
        log.info("GET request to admin/comment/{comId} endpoint");
        commentAdminService.deleteComment(comId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Комментарий " + comId + " удален");
    }
}