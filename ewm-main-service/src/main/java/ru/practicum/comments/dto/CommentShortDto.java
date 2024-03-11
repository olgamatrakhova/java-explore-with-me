package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.users.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {
    private long id;
    private String text;
    private UserDto author;
    private String createTime;
}