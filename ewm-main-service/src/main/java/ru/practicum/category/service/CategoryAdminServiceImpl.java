package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotUniqueException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("Вызов добавления категории addCategory({})", categoryDto);
        if (categoryRepository.existsByName(categoryDto.getName())) {
            log.error("Наименование категории не уникально {}", categoryDto.getName());
            throw new NotUniqueException("Категории с названием " + categoryDto.getName() + " уже существуют");
        } else {
            Category category = categoryRepository.saveAndFlush(CategoryMapper.toCategory(categoryDto));
            log.info("Категория - {} - сохранена", category.getName());
            return CategoryMapper.toCategoryDto(category);
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        log.info("Вызов удаления категории deleteCategory({})", catId);
        if (!categoryRepository.existsById(catId)) {
            log.error("Категории с id = {} не существует", catId);
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Нельзя удалить событие со связными событиями");
        }
        try {
            categoryRepository.deleteById(catId);
            log.info("Категории с id = {} удалена", catId);
        } catch (Exception e) {
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Вызов обновления категории updateCategory({},{})", catId, categoryDto);
        Category category = categoryRepository.findById(catId).orElseThrow(()
                -> new NotFoundException("Категории с id = " + catId + " не существует"));
        if (!category.getName().equals(categoryDto.getName()) &&
                categoryRepository.existsByName(categoryDto.getName())) {
            log.error("Категория с наименование - {} - не уникальна", categoryDto.getName());
            throw new NotUniqueException("Категория с наименование - " + categoryDto.getName() + " - не уникальна");
        }
        category.setName(categoryDto.getName());
        log.info("Категории {} обновлена", category.getName());
        return CategoryMapper.toCategoryDto(category);
    }
}