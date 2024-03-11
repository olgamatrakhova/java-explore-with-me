package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryAdminService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories")
@Slf4j
public class CategoryAdminController {
    private final CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Validated CategoryDto categoryDto,
                                                     BindingResult bindingResult) {
        log.info("POST request to /admin/categories endpoint");
        if (bindingResult.hasErrors()) {
            log.error("Не верное наименование категории");
            return ResponseEntity.badRequest().body((categoryDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryAdminService.addCategory(categoryDto));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCategory(@PathVariable @Positive Long catId) {
        log.info("DELETE request to /admin/categories/{catId} endpoint");
        categoryAdminService.deleteCategory(catId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Удалена категория: " + catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                                        @RequestBody @Validated CategoryDto categoryDto) {
        log.info("PATCH request to /admin/categories/{catId} endpoint");
        return ResponseEntity.status(HttpStatus.OK).body(categoryAdminService.updateCategory(catId, categoryDto));
    }
}