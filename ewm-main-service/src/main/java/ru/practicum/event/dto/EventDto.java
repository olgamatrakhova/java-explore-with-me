package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.model.Categories;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String annotation;
    private Categories categories;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private User initiator;
    private Boolean paid;
    private String title;
    private Long views;
}