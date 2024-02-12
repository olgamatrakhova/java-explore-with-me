package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.users.dto.UserShortDto;

@Data
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoriesDto categories;
    private Long confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}