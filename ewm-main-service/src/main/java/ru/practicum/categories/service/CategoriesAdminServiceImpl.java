package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotUniqueException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoriesAdminServiceImpl implements CategoriesAdminService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoriesDto addCategories(CategoriesDto categoriesDto) {
        log.info("Вызов добавления категории addCategories({})", categoriesDto);
        if (categoriesRepository.existsByName(categoriesDto.getName())) {
            log.error("Наименование категории не уникально {}", categoriesDto.getName());
            throw new NotUniqueException("Категории с названием " + categoriesDto.getName() + " уже существуют");
        } else {
            Categories categories = categoriesRepository.saveAndFlush(CategoriesMapper.toCategories(categoriesDto));
            log.info("Категория - {} - сохранена", categories.getName());
            return CategoriesMapper.toCategoriesDto(categories);
        }
    }

    @Override
    public void deleteCategories(Long catId) {
        log.info("Вызов удаления категории deleteCategories({})", catId);
        if (!categoriesRepository.existsById(catId)) {
            log.error("Категории с id = {} не существует", catId);
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
        if (eventRepository.existsByCategoriesId(catId)) {
            throw new ConflictException("Нельзя удалить событие со связными событиями");
        }
        if (!categoriesRepository.existsById(catId)) {
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
        try {
            categoriesRepository.deleteById(catId);
            log.info("Категории с id = {} удалена", catId);
        } catch (Exception e) {
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
    }

    @Override
    public CategoriesDto updateCategories(Long catId, CategoriesDto categoriesDto) {
        log.info("Вызов обновления категории updateCategories({},{})", catId, categoriesDto);
        try {
            Categories categories = categoriesRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("Категории с id = " + catId + " не существует"));
            if (!categories.getName().equals(categoriesDto.getName()) &&
                    categoriesRepository.existsByName(categoriesDto.getName())) {
                log.error("Категория с наименование - {} - не уникальна", categoriesDto.getName());
                throw new NotUniqueException("Категория с наименование - " + categoriesDto.getName() + " - не уникальна");
            }
            categories.setName(categoriesDto.getName());
            log.info("Категории {} обновлена", categories.getName());
            return CategoriesMapper.toCategoriesDto(categories);
        } catch (Exception e) {
            throw new NotFoundException("Категории с id = " + catId + " не существует");
        }
    }
}