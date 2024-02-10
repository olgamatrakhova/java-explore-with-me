package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.status.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getAllEventsByInitiatorId(long userId, Pageable pageable);

    Optional<Event> getAllEventsByIdAndInitiatorId(long eventId, long userId);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN ?1 OR ?1 IS null) " +
            "AND (e.eventStatus IN ?2 OR ?2 IS null) " +
            "AND (e.categories.id IN ?3 OR ?3 IS null) " +
            "AND (e.eventDate > ?4 OR ?4 IS null) " +
            "AND (e.eventDate < ?5 OR ?5 IS null) ")
    List<Event> getAllEventsByParam(List<Long> users, List<EventStatus> states, List<Long> categories,
                                    LocalDateTime start, LocalDateTime end, Pageable pageable);

    boolean existsByIdAndInitiatorId(long eventId, long userId);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((?1 IS null) OR ((lower(e.annotation) LIKE concat('%', lower(?1), '%')) OR (lower(e.description) LIKE concat('%', lower(?1), '%')))) " +
            "AND (e.categories.id IN ?2 OR ?2 IS null) " +
            "AND (e.paid = ?3 OR ?3 IS null) " +
            "AND (e.eventDate > ?4 OR ?4 IS null) AND (e.eventDate < ?5 OR ?5 IS null) " +
            "AND (?6 = false OR ((?6 = true AND e.participantLimit > (SELECT count(*) FROM Request AS r WHERE e.id = r.event.id))) " +
            "OR (e.participantLimit > 0 )) ")
    List<Event> findAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                             LocalDateTime end, Boolean onlyAvailable, String sort, Pageable pageable);

    boolean existsByCategoriesId(long catId);
}