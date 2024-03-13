package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCountDto {
    private Long eventId;
    private Long commentCount;
}