package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.utils.Utils.createPageRequestAsc;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoriesPublicServiceImpl implements CategoriesPublicService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<CategoriesDto> getAllCategories(Integer from, Integer size) {
        log.info("Вызов получения всех категорий getAllCategories({},{})", from, size);
        List<Categories> categoriesList = categoriesRepository.findAllCategories(createPageRequestAsc(from, size));
        log.info("Вернулось {} категорий", categoriesList.size());
        return CategoriesMapper.toListCategoriesDto(categoriesList);
    }

    @Override
    public CategoriesDto getCategoriesById(Long catId) {
        log.info("Вызов категории по идентификатору getCategoriesById({})", catId);
        try {
            Categories categories = categoriesRepository.findById(catId).orElseThrow(() -> {
                log.error("Категории с id = {} не существует", catId);
                return new NotFoundException("Категории с id =" + catId + " не существует");
            });
            log.info("Категория обновлена - {}", categories.getName());
            return CategoriesMapper.toCategoriesDto(categories);
        } catch (Exception e) {
            throw new NotFoundException("Категории с id =" + catId + " не существует");
        }
    }
}