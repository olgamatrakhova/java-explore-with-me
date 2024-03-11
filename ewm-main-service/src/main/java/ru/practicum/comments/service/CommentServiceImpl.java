package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.utils.Utils.createPageRequestSortAsc;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    @Override
    public CommentDto getCommentById(Long comId) {
        log.info("Вызов получения комментария по ид getCommentById({})", comId);
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> {
            log.error("Комментарий с id = {} не найден", comId);
            return new NotFoundException("Comment not found");
        });
        log.info("Комментарий найден {}", comId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentShortDto> getCommentsByEvent(Long eventId, int from, int size) {
        log.info("Вызов получения всех комментариев события getCommentsByEven({},{},{})", eventId, from, size);
        if (eventRepository.existsById(eventId)) {
            log.error("События с id = {} не существует", eventId);
            throw new NotFoundException("События не существует");
        }
        Pageable pageable = createPageRequestSortAsc("createTime", from, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        log.info("Найдено комментариев по событию - {}", comments.size());
        return CommentMapper.toListCommentShortDto(comments);
    }
}