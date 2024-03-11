package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentCountDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.EventStatus;
import ru.practicum.exception.ConflictException;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentCreateDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentCreateDto commentDto) {
        log.info("Вызов создания комментария addComment({},{},{})", userId, eventId, commentDto);
        Comment comment = CommentMapper.toComment(commentDto);
        User author = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id = {} не найден", userId);
            return new NotFoundException("Пользователь не зарегистрирован");
        });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Событие с id = {} - не существует", eventId);
                    return new NotFoundException("Событие не существует");
                });
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Статус события - {},а должен быть PUBLISHED", event.getState());
            throw new ConflictException("Событие не опубликовано");
        }
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreateTime(LocalDateTime.now().withNano(0));
        log.info("Добавлен новый комментарий {}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long comId) {
        log.info("Вызов удаления комментария deleteComment({},{})", userId, comId);
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> {
                    log.error("События с id = {} не существует", comId);
                    return new NotFoundException("События не существует");
                });
        if (!comment.getAuthor().getId().equals(userId)) {
            log.error("У пользователя нет комментариев");
            throw new ConflictException("У пользователя нет комментариев");
        }
        log.info("Комментарий с id = {} удален", comId);
        commentRepository.deleteById(comId);
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long userId, Long comId, CommentCreateDto commentCreateDto) {
        log.info("Вызов обновления комментария updateComment({},{},{})", userId, comId, commentCreateDto);
        Comment newComment = CommentMapper.toComment(commentCreateDto);
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> {
            log.error("Комментарий с id = {} не найден", comId);
            return new NotFoundException("Комментарий не найден");
        });
        if (!comment.getAuthor().getId().equals(userId)) {
            log.error("У пользователя нет комментариев");
            throw new ConflictException("У пользователя нет комментариев");
        }
        comment.setText(newComment.getText());
        comment.setCreateTime(LocalDateTime.now().withNano(0));
        log.info("Комментарий с id = {} обновлен", comId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public Map<Long, Long> getCommentCount(Collection<Event> list) {
        List<Long> listEventId = list.stream().map(Event::getId).collect(Collectors.toList());
        List<CommentCountDto> countList = commentRepository.findAllCommentCount(listEventId);
        return countList.stream().collect(Collectors.toMap(CommentCountDto::getEventId, CommentCountDto::getCommentCount));
    }
}