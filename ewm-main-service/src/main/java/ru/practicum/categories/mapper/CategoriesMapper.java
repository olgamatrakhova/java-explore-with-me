package ru.practicum.categories.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.categories.model.Categories;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoriesMapper {
    public CategoriesDto toCategoriesDto(Categories categories) {
        return CategoriesDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }

    public Categories toCategories(CategoriesDto categoriesDto) {
        return Categories.builder()
                .name(categoriesDto.getName())
                .build();
    }

    public List<CategoriesDto> toListCategoriesDto(List<Categories> list) {
        return list.stream().map(CategoriesMapper::toCategoriesDto).collect(Collectors.toList());
    }
}