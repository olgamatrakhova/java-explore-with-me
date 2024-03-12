package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.dto.CommentCreateDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.users.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.Utils.FORMATTER;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentCreateDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .event(EventMapper.toEventComment(comment.getEvent()))
                .createTime(comment.getCreateTime().format(FORMATTER))
                .text(comment.getText())
                .build();
    }

    public List<CommentDto> toListCommentDto(List<Comment> list) {
        return list.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    public CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .createTime(comment.getCreateTime().format(FORMATTER))
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }

    public List<CommentShortDto> toListCommentShortDto(List<Comment> list) {
        return list.stream().map(CommentMapper::toCommentShortDto).collect(Collectors.toList());
    }
}