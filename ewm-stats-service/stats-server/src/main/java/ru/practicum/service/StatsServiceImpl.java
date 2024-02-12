package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.exception.WrongTimeException;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsServiceRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.mapper.StatsMapper.toStats;
import static ru.practicum.mapper.StatsMapper.toStatsDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsServiceRepository statsServiceRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public StatsDto addStat(StatsDto statsDto) {
        log.info("Вызов addStat ({})", statsDto);
        Stats stats = statsServiceRepository.save(toStats(statsDto));
        log.info("Успех при сохранении addStats {}", stats);
        return toStatsDto(stats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStat(String start, String end, List<String> uris, boolean unique) throws UnsupportedEncodingException {
        log.info("Вызов getStat ({},{},{},{})", start, end, uris, unique);
        LocalDateTime startDate = decodeStrToDate(start);
        LocalDateTime endDate = decodeStrToDate(end);
        if (startDate.isAfter(endDate)) {
            log.error("Ошибка: Дата начала не может быть позже даты окончания (start = {}, end = {})", startDate, endDate);
            throw new WrongTimeException("Дата начала не может быть позже даты окончания");
        }
        if (uris.isEmpty()) {
            if (unique) {
                log.info("Успех при получении getStat - unique = true, uris empty");
                return statsServiceRepository.findAllByTimestampBetweenStartAndEndWithUniqueIp(startDate, endDate);
            } else {
                log.info("Успех при получении getStat - unique = false, uris empty");
                return statsServiceRepository.findAllByTimestampBetweenStartAndEndWhereIpNotUnique(startDate, endDate);
            }
        } else {
            if (unique) {
                log.info("Успех при получении getStat - unique = true, uris not empty");
                return statsServiceRepository.findAllByTimestampBetweenStartAndEndWithUrisUniqueIp(startDate, endDate, uris);
            } else {
                log.info("Успех при получении getStat - unique = false, uris not empty");
                return statsServiceRepository.findAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(startDate, endDate, uris);
            }
        }
    }

    private LocalDateTime decodeStrToDate(String value) throws UnsupportedEncodingException {
        return LocalDateTime.parse(URLDecoder.decode(value, "UTF-8"), FORMATTER);
    }
}