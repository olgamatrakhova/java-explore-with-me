package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
@Slf4j
public class CategoryPublicController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to - /categories - endpoint");
        return ResponseEntity.ok(categoryPublicService.getAllCategory(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("GET request to - /categories/{catId} - endpoint");
        return ResponseEntity.ok(categoryPublicService.getCategoryById(catId));
    }
}