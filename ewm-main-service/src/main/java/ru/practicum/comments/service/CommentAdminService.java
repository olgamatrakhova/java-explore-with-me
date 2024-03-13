package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface CommentAdminService {
    void deleteComment(Long comId);

    List<CommentDto> searchComment(String text);

    List<CommentDto> findCommentByUserId(Long userId);
}