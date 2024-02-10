package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.event.location.dto.LocationDto;
import ru.practicum.event.status.EventStatus;
import ru.practicum.users.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoriesDto categories;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStatus eventStatus;
    private String title;
    private Long views;
    private Long confirmedRequests;
    private String publishedOn;
}