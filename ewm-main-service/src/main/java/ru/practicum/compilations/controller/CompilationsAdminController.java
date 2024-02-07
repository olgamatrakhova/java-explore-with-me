package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.NewCompilationsDto;
import ru.practicum.compilations.service.CompilationsAdminService;

@RestController
@Validated
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationsAdminController {
    private final CompilationsAdminService compilationsAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompilationsDto> addCompilations(@RequestBody @Validated NewCompilationsDto newCompilationsDto) {
        log.info("POST request to /admin/compilations endpoint");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationsAdminService.addCompilations(newCompilationsDto));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCompilations(@PathVariable Long compId) {
        log.info("DELETE request to /admin/endpoint/{compId}");
        compilationsAdminService.deleteCompilations(compId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Подборка удалена: " + compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationsDto> updateCompilations(@PathVariable Long compId,
                                                              @RequestBody @Validated NewCompilationsDto newCompilationsDto) {
        log.info("PATCH request to /admin/compilations/{compId} endpoint");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationsAdminService.updateCompilations(compId, newCompilationsDto));
    }
}