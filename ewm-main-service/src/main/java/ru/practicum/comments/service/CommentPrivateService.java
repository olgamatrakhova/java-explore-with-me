package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentCreateDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.event.model.Event;

import java.util.Collection;
import java.util.Map;

public interface CommentPrivateService {

    CommentDto addComment(Long userId, Long eventId, CommentCreateDto commentDto);

    void deleteComment(Long userId, Long comId);

    CommentDto updateComment(Long userId, Long comId, CommentCreateDto commentCreateDto);

    Map<Long, Long> getCommentCount(Collection<Event> list);
}