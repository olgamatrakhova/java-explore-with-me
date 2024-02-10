package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoriesDto;

import java.util.List;

public interface CategoriesPublicService {
    List<CategoriesDto> getAllCategories(Integer from, Integer size);

    CategoriesDto getCategoriesById(Long catId);
}