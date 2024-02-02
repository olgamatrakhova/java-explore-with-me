package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.exception.WrongTimeException;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsServiceRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mapper.StatsMapper.toStats;
import static ru.practicum.mapper.StatsMapper.toStatsDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsServiceRepository statsServiceRepository;

    @Override
    public StatsDto addStat(StatsDto statsDto) {
        log.info("Вызов addStat");
        Stats stats = statsServiceRepository.save(toStats(statsDto));
        log.info("Успех при сохранении addStats {}", stats);
        return toStatsDto(stats);
    }

    @Override
    public List<StatsResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Вызов getStat");
        if (start.isAfter(end)) {
            log.error("Ошибка: Дата начала не может быть позже даты окончания");
            throw new WrongTimeException("Дата начала не может быть позже даты окончания");
        }
        if (uris.isEmpty()) {
            if (unique) {
                log.info("Успех при получении getStat - unique = true, uris empty");
                return statsServiceRepository.getAllByTimestampBetweenStartAndEndWithUniqueIp(start, end);
            } else {
                log.info("Успех при получении getStat - unique = false, uris empty");
                return statsServiceRepository.getAllByTimestampBetweenStartAndEndWhereIpNotUnique(start, end);
            }
        } else {
            if (unique) {
                log.info("Успех при получении getStat - unique = true, uris not empty");
                return statsServiceRepository.getAllByTimestampBetweenStartAndEndWithUrisUniqueIp(start, end, uris);
            } else {
                log.info("Успех при получении getStat - unique = false, uris not empty");
                return statsServiceRepository.getAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(start, end, uris);
            }
        }
    }
}