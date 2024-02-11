package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventPatchDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.location.repository.LocationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.EventStatus;
import ru.practicum.event.status.EventStatusUser;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.RequestStatus;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestShortUpdateDto;
import ru.practicum.request.dto.RequestUpdateDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stat.service.StatsService;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.utils.Utils.createPageRequestAsc;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatsService statsService;

    @Override
    public EventFullDto addEvent(Long userId, EventRequestDto eventRequestDto) {
        log.info("Создание события addEvent({},{}", userId, eventRequestDto);
        Event event = EventMapper.toEvent(eventRequestDto);
        if (event.getCategories().getId() != null) {
            event.setCategories(categoriesRepository.findById(event.getCategories().getId())
                    .orElseThrow(() -> new NotFoundException("Категории не существует")));
        } else {
            categoriesRepository.saveAndFlush(event.getCategories());
        }
        event.setEventStatus(EventStatus.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        if (event.getLocation() != null) {
            event.setLocation(locationRepository.save(event.getLocation()));
        }
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        } else {
            event.setInitiator(userRepository.findById(userId).get());
        }
        log.info("Событие {} создано", event);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventByUserId(Long userId, int from, int size) {
        log.info("Вызов списка событий которые создал пользователь getEventByUserId({},{},{})", userId, from, size);
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        List<Event> events = eventRepository.getAllEventsByInitiatorId(userId, createPageRequestAsc(from, size));
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(events);
        Map<Long, Long> mapView = statsService.toView(events);
        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(mapView.getOrDefault(event.getId(), 0L));
        }
        log.info("Возвращено {} событий пользователя", events.size());
        return EventMapper.toListEventFullDto(events);
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        log.info("Вызов поиска события по ид по пользователю getEventByUserIdAndEventId({},{})", userId, eventId);
        Event event = eventRepository.getAllEventsByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Событие не существует");
            return new NotFoundException("Событие не существует");
        });
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        Map<Long, Long> mapView = statsService.toView(List.of(event));
        event.setView(mapView.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        log.info("Вернулось событие {}", event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {
        log.info("Вызов обновления события updateEvent({},{},{})", userId, eventId, eventUpdateDto);
        EventPatchDto eventPatchDto = EventMapper.toEventFromUpdateEvent(eventUpdateDto);
        Event event = eventRepository.getAllEventsByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Событие не существует");
            return new NotFoundException("Событие не существует");
        });
        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Пользователь не является владельцем события и не может его изменять");
            throw new ConflictException("Пользователь не является владельцем события и не может его изменять");
        }
        if (event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            log.error("Нельзя отредактировать событие со статусом EventStatus.PUBLISHED");
            throw new ConflictException("Нельзя отредактировать событие со статусом Опубликовано");
        }
        LocalDateTime eventTime = eventPatchDto.getEventDate();
        if (eventTime != null) {
            if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
                log.error("Дата и время события не могут быть раньше, чем через два часа от текущего момента");
                throw new BadRequestException("Дата и время события не могут быть раньше, чем через два часа от текущего момента");
            }
            event.setEventDate(eventTime);
        }
        EventStatusUser eventStatusUser = eventPatchDto.getStateAction();
        if (eventStatusUser != null) {
            if (eventStatusUser.equals(EventStatusUser.SEND_TO_REVIEW)) {
                event.setEventStatus(EventStatus.PENDING);
            }
            if (eventStatusUser.equals(EventStatusUser.CANCEL_REVIEW)) {
                event.setEventStatus(EventStatus.CANCELED);
            }
        }
        if (eventPatchDto.getPaid() != null) {
            event.setPaid(eventPatchDto.getPaid());
        }
        if (eventPatchDto.getRequestModeration() != null) {
            event.setRequestModeration(eventPatchDto.getRequestModeration());
        }
        if (eventPatchDto.getAnnotation() != null && !eventPatchDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventPatchDto.getAnnotation());
        }
        if (eventPatchDto.getTitle() != null && !eventPatchDto.getTitle().isBlank()) {
            event.setTitle(eventPatchDto.getTitle());
        }
        if (eventPatchDto.getDescription() != null && !eventPatchDto.getDescription().isBlank()) {
            event.setDescription(eventPatchDto.getDescription());
        }
        if (eventPatchDto.getLocation() != null) {
            event.setLocation(locationRepository.findByLatAndLon(eventPatchDto.getLocation().getLat(), eventPatchDto.getLocation().getLon())
                    .orElse(locationRepository.save(eventPatchDto.getLocation())));
        }
        if (eventPatchDto.getCategories() != null) {
            event.setCategories(categoriesRepository.findById(eventPatchDto.getCategories())
                    .orElseThrow(() -> {
                        log.error("Категории не существует");
                        return new NotFoundException("Категории не существует");
                    }));
        }
        Map<Long, Long> view = statsService.toView(List.of(event));
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        log.info("Событие {} изменено ", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        log.info("Вызов просмотр заявок по событию пользователя getRequestByUserIdAndEventId({},{})", userId, eventId);
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            log.error("Пользователю не доступно событие");
            throw new ConflictException("Пользователю не доступно событие");
        }
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("Список запросов по событию пользователя = {}", requests.size());
        return RequestMapper.toListRequestDto(requests);
    }

    @Override
    public RequestShortUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto) {
        log.info("Вызов обновления статусов по заявкам пользователя updateRequestByOwner({},{},{})", userId, eventId, requestShortDto);
        RequestShortDto requestShort = RequestMapper.toRequestShort(requestShortDto);
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id = {} не существует", userId);
            throw new NotFoundException("пользователь не существует");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("События с id = {} не существует", eventId);
            return new NotFoundException("События не существует");
        });
        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Пользователю не принадлежит событие");
            throw new ConflictException("Пользователю не принадлежит событие");
        }
        int confirmedRequest = statsService.toConfirmedRequest(List.of(event)).values().size();
        if (event.getParticipantLimit() != 0 && confirmedRequest >= event.getParticipantLimit()) {
            log.error("Свободных мест на событие больше нет");
            throw new ConflictException("Свободных мест на событие больше нет");
        }
        RequestUpdateDto requestUpdateDto = new RequestUpdateDto();
        requestShort.getRequestIds().forEach(requestId -> {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> {
                log.error("Заявки с id = {} не существует", requestId);
                return new NotFoundException("Заявки нет");
            });
            if (requestShort.getRequestStatus().equals(RequestStatus.CONFIRMED)) {
                request.setRequestStatus(RequestStatus.CONFIRMED);
                requestUpdateDto.getConfirmedRequest().add(request);
            }
            if (requestShort.getRequestStatus().equals(RequestStatus.REJECTED)) {
                request.setRequestStatus(RequestStatus.REJECTED);
                requestUpdateDto.getCanselRequest().add(request);
            }
        });
        log.info("Заявка {} обновлена", requestUpdateDto);
        return RequestMapper.toRequestShortUpdateDto(requestUpdateDto);
    }
}