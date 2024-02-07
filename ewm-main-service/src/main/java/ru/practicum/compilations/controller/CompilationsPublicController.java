package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.service.CompilationsPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationsPublicController {
    private final CompilationsPublicService service;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationsDto> getCompilationsById(@PathVariable Long compId) {
        log.info("GET request to /compilations/{compId} endpoint");
        CompilationsDto response = service.getCompilationsById(compId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompilationsDto>> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to /compilations endpoint");
        List<CompilationsDto> list = service.getAllCompilations(pinned, from, size);
        return ResponseEntity.ok(list);
    }
}