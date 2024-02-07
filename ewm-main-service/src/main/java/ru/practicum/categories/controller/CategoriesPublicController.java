package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.categories.service.CategoriesPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
@Slf4j
public class CategoriesPublicController {
    private final CategoriesPublicService categoriesPublicService;

    @GetMapping
    public ResponseEntity<List<CategoriesDto>> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to - /categories - endpoint");
        return ResponseEntity.ok(categoriesPublicService.getAllCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoriesDto> getCategoriesById(@PathVariable Long catId) {
        log.info("GET request to - /categories/{catId} - endpoint");
        return ResponseEntity.ok(categoriesPublicService.getCategoriesById(catId));
    }
}