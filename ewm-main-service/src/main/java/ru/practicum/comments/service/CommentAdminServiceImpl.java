package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void deleteComment(Long comId) {
        log.info("Вызов удаления комментария deleteComment({})", comId);
        if (!commentRepository.existsById(comId)) {
            log.error("Нет комментария с id = {}", comId);
            throw new NotFoundException("Комментарий не найден");
        }
        log.info("Комментарий с id = {} удален", comId);
        commentRepository.deleteById(comId);
    }

    @Override
    public List<CommentDto> searchComment(String text) {
        log.info("Вызов поиска комментария по тексту searchComment({})", text);
        List<Comment> list = commentRepository.findAllByText(text);
        log.info("С заданным текстом найдено {} комментариев", list.size());
        return CommentMapper.toListCommentDto(list);
    }

    @Override
    public List<CommentDto> findCommentByUserId(Long userId) {
        log.info("Вызов получение комментариев пользователя findCommentByUserId({})", userId);
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id = {} не существует", userId);
            throw new NotFoundException("Пользователь не найден");
        }
        List<Comment> list = commentRepository.findAllByAuthorId(userId);
        log.info("Найдены комментарии пользователя - {}", list.size());
        return CommentMapper.toListCommentDto(list);
    }
}