package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsServiceController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<StatsDto> addStatsEvent(@RequestBody @Validated StatsDto statsDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос: addStatsEvent({})", statsDto);
        StatsDto statsEvent = statsService.addStat(statsDto);
        log.info("POST request /hit/ endpoint - statDto = {}", statsDto);
        return new ResponseEntity<>(statsEvent, HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsResponseDto>> getStatsEvent(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                                @RequestParam(defaultValue = "") List<String> uris,
                                                                @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получение статистики по посещениям: getStatsEvent({},{},{},{})", start, end, uris, unique);
        List<StatsResponseDto> stats = statsService.getStat(start, end, uris, unique);
        log.info("GET request /stats/ endpoint - stat size = {}", stats.size());
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}