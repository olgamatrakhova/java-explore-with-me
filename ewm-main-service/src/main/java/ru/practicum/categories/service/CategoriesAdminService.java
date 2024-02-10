package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoriesDto;

public interface CategoriesAdminService {
    CategoriesDto addCategories(CategoriesDto categoriesDto);

    void deleteCategories(Long catId);

    CategoriesDto updateCategories(Long catId, CategoriesDto categoriesDto);
}
