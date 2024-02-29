package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.utils.Utils.createPageRequestAsc;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategory(Integer from, Integer size) {
        log.info("Вызов получения всех категорий getAllCategory({},{})", from, size);
        List<Category> categoryList = categoryRepository.findAllCategory(createPageRequestAsc(from, size));
        log.info("Вернулось {} категорий", categoryList.size());
        return CategoryMapper.toListCategoryDto(categoryList);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("Вызов категории по идентификатору getCategoryById({})", catId);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(() -> {
                log.error("Категории с id = {} не существует", catId);
                return new NotFoundException("Категории с id =" + catId + " не существует");
            });
            log.info("Категория обновлена - {}", category.getName());
            return CategoryMapper.toCategoryDto(category);
        } catch (Exception e) {
            throw new NotFoundException("Категории с id =" + catId + " не существует");
        }
    }
}