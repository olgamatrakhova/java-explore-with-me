package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsServiceRepository extends JpaRepository<Stats, Long> {
    @Query("select new ru.practicum.dto.StatsResponseDto(s.ip, s.uri, count(distinct s.ip)) " +
            "from Stats as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.ip, s.uri " +
            "order by count(distinct s.ip) desc")
    List<StatsResponseDto> findAllByTimestampBetweenStartAndEndWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsResponseDto(s.ip, s.uri, count(s.ip)) " +
            "from Stats as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.ip, s.uri " +
            "order by count(s.ip) desc ")
    List<StatsResponseDto> findAllByTimestampBetweenStartAndEndWhereIpNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsResponseDto(s.ip, s.uri, count(distinct s.ip)) " +
            "from Stats as s " +
            "where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.ip, s.uri " +
            "order by count(distinct s.ip) desc ")
    List<StatsResponseDto> findAllByTimestampBetweenStartAndEndWithUrisUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatsResponseDto(s.ip, s.uri, count(s.ip)) " +
            "from Stats as s " +
            "where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.ip, s.uri " +
            "order by count(s.ip) desc ")
    List<StatsResponseDto> findAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}