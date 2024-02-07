package ru.practicum.event.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.location.model.Location;

import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> getByLatAndLon(double lat, double lon);
}