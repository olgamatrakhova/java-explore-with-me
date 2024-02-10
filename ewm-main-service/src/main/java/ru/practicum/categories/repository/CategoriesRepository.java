package ru.practicum.categories.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.categories.model.Categories;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    boolean existsByName(String name);

    @Query("select c from Categories c ")
    List<Categories> findAllCategories(Pageable pageable);
}