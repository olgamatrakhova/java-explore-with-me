package ru.practicum.categories.controller;

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
import ru.practicum.categories.dto.CategoriesDto;
import ru.practicum.categories.service.CategoriesAdminService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories")
@Slf4j
public class CategoriesAdminController {
    private final CategoriesAdminService categoriesAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoriesDto> addCategories(@RequestBody @Validated CategoriesDto categoriesDto,
                                                       BindingResult bindingResult) {
        log.info("POST request to /admin/categories endpoint");
        if (bindingResult.hasErrors()) {
            log.error("Не верное наименование категории");
            return ResponseEntity.badRequest().body((categoriesDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesAdminService.addCategories(categoriesDto));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCategories(@PathVariable @Positive Long catId) {
        log.info("DELETE request to /admin/categories/{catId} endpoint");
        categoriesAdminService.deleteCategories(catId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Удалена категория: " + catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoriesDto> updateCategories(@PathVariable Long catId,
                                                          @RequestBody @Validated CategoriesDto categoriesDto) {
        log.info("PATCH request to /admin/categories/{catId} endpoint");
        return ResponseEntity.status(HttpStatus.OK).body(categoriesAdminService.updateCategories(catId, categoriesDto));
    }
}