package ru.practicum.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilations.model.Compilations;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    @Query("select c from Compilations c where c.pinned = ?1")
    List<Compilations> getAllByPinned(Boolean pinned, Pageable pageable);

    boolean getByTitle(String title);
}