package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.model.Request;

import java.util.List;
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT new ru.practicum.request.dto.ConfirmedRequestShortDto(r.event.id , count(r.id)) " +
            "FROM Request r " +
            "WHERE r.event.id in ?1 " +
            "AND r.requestStatus = 'CONFIRMED' " +
            "GROUP BY r.event.id ")
    List<ConfirmedRequestShortDto> countByEventId(List<Long> longs);

    boolean getByRequesterIdAndEventId(long userId, long eventId);

    List<Request> getAllByRequesterId(long userId);

    List<Request> getAllByEventId(long eventId);
}