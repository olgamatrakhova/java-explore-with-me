package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.event.dto.EventAdminDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventPatchDto;
import ru.practicum.event.dto.EventRequestAdminDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.location.mapper.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.users.mapper.UserMapper;

import static ru.practicum.utils.Utils.FORMATTER;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    public Event toEvent(EventRequestDto receivedDto) {
        return Event.builder()
                .eventDate(receivedDto.getEventDate())
                .annotation(receivedDto.getAnnotation())
                .categories(Categories.builder().id(receivedDto.getCategories()).build())
                .paid(receivedDto.isPaid())
                .description(receivedDto.getDescription())
                .title(receivedDto.getTitle())
                .participantLimit(receivedDto.getParticipantLimit())
                .requestModeration(receivedDto.isRequestModeration())
                .location(LocationMapper.toLocation(receivedDto.getLocation()))
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto build = EventFullDto.builder()
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getView())
                .eventStatus(event.getEventStatus())
                .annotation(event.getAnnotation())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .paid(event.getPaid())
                .title(event.getTitle())
                .id(event.getId())
                .categories(CategoriesMapper.toCategoriesDto(event.getCategories()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .build();

        if (event.getPublishedOn() != null) {
            build.setPublishedOn(event.getPublishedOn().format(FORMATTER));
        }
        return build;
    }

    public List<EventFullDto> toListEventFullDto(List<Event> list) {
        return list.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public EventPatchDto toEventFromUpdateEvent(EventUpdateDto eventUpdateDto) {
        EventPatchDto event = EventPatchDto.builder()
                .stateAction(eventUpdateDto.getStateAction())
                .annotation(eventUpdateDto.getAnnotation())
                .participantLimit(eventUpdateDto.getParticipantLimit())
                .requestModeration(eventUpdateDto.getRequestModeration())
                .categories(eventUpdateDto.getCategories())
                .description(eventUpdateDto.getDescription())
                .title(eventUpdateDto.getTitle())
                .paid(eventUpdateDto.getPaid())
                .eventDate(eventUpdateDto.getEventDate())
                .build();
        event.setLocation(eventUpdateDto.getLocation() == null ? null : LocationMapper.toLocation(eventUpdateDto.getLocation()));
        return event;
    }

    public static EventAdminDto toEventAdminFromAdminDto(EventRequestAdminDto eventAdmin) {
        EventAdminDto event = EventAdminDto.builder()
                .stateAction(eventAdmin.getStateAction())
                .annotation(eventAdmin.getAnnotation())
                .participantLimit(eventAdmin.getParticipantLimit())
                .requestModeration(eventAdmin.getRequestModeration())
                .categories(eventAdmin.getCategories())
                .description(eventAdmin.getDescription())
                .eventDate(eventAdmin.getEventDate())
                .paid(eventAdmin.getPaid())
                .title(eventAdmin.getTitle())
                .build();
        event.setLocation(eventAdmin.getLocation() == null ? null : LocationMapper.toLocation(eventAdmin.getLocation()));
        return event;
    }

    public EventDto toEventShort(Event event, Long view, Long confirmedRequests) {
        return EventDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .confirmedRequests(confirmedRequests)
                .views(view)
                .annotation(event.getAnnotation())
                .categories(event.getCategories())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public EventShortDto toEventShortDto(EventDto eventDto) {
        return EventShortDto.builder()
                .initiator(UserMapper.toUserShortDto(eventDto.getInitiator()))
                .views(eventDto.getViews())
                .eventDate(eventDto.getEventDate().format(FORMATTER))
                .annotation(eventDto.getAnnotation())
                .title(eventDto.getTitle())
                .categories(CategoriesMapper.toCategoriesDto(eventDto.getCategories()))
                .confirmedRequests(eventDto.getConfirmedRequests())
                .id(eventDto.getId())
                .paid(eventDto.getPaid())
                .build();
    }

    public static List<EventShortDto> toListEventShortDto(List<EventDto> list) {
        return list.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventDto toEventShort(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getView())
                .annotation(event.getAnnotation())
                .categories(event.getCategories())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static List<EventDto> toListEventShort(List<Event> list) {
        return list.stream().map(EventMapper::toEventShort).collect(Collectors.toList());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .categories(CategoriesMapper.toCategoriesDto(event.getCategories()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().toString())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getView())
                .build();
    }
}