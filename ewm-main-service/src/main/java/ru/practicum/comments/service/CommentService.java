package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;

import java.util.List;

public interface CommentService {
    CommentDto getCommentById(Long comId);

    List<CommentShortDto> getCommentsByEvent(Long eventId, int from, int size);
}