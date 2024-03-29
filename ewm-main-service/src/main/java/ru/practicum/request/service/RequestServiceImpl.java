package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.EventStatus;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.RequestStatus;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getAllRequests(Long userId) {
        log.info("Вызов получения всех запросов по пользователю getAllRequests({})", userId);
        List<RequestDto> requests = RequestMapper.toListRequestDto(requestRepository.findAllByRequesterId(userId));
        log.info("Возвращено {} запросов", requests.size());
        return requests;
    }

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        log.info("Вызов добавление запроса пользователя addRequest({},{})", userId, eventId);
        RequestStatus requestStatus;
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователя с id = {} не существует", userId);
            return new NotFoundException("Пользователя с id = " + userId + " не существует");
        });
        Event event = eventRepository.findAllEventsById(eventId).orElseThrow(() -> {
            log.error("Событие c id = {} не найдено", eventId);
            throw new NotFoundException("Событие c id =" + eventId + " не найдено");
        });
        if (event.getInitiator().getId().equals(userId)) {
            log.error("Создатель мероприятия не может подавать запрос на мероприятие");
            throw new ConflictException("Создатель мероприятия не может подавать запрос на мероприятие");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Статус события {} - он должен быть PUBLISHED", event.getState());
            throw new ConflictException("Статус события " + event.getState() + " - он должен быть PUBLISHED");
        }
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            log.error("Пользователь уже подавал заявку");
            throw new ConflictException("Пользователь уже подавал заявку");
        }
        requestStatus = (!event.getRequestModeration() || event.getParticipantLimit() == 0) ? RequestStatus.CONFIRMED : RequestStatus.PENDING;
        List<ConfirmedRequestShortDto> confirmed = requestRepository.countByEventId(List.of(eventId));
        if (confirmed.size() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            log.error("Список участников заполнен");
            throw new ConflictException("Список участников переполнен");
        }

        Request request = requestRepository.save(Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now().withNano(0))
                .status(requestStatus)
                .build());
        log.info("Заявка на событие {} создана", request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Вызов отмены регистрации в событии cancelRequest({},{})", userId, requestId);
        if (!userRepository.existsById(userId)) {
            log.error("Пользователя с id = {} не существует", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " не существует");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Заявки с id = {} не существует", requestId);
            return new NotFoundException("Заявки с id = " + requestId + " не существует");
        });
        request.setStatus(RequestStatus.CANCELED);
        log.info("Заявка с id = {}, отменена, status = {}", requestId, request.getStatus());
        return RequestMapper.toRequestDto(request);
    }
}