package ru.practicum.categories.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Categories;

import java.util.List;
@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    boolean getByName(String name);

    @Query("select c from Categories c ")
    List<Categories> getAllCategories(Pageable pageable);
}