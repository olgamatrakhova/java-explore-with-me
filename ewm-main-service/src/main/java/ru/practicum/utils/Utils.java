package ru.practicum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

public class Utils {
    public static final String TIME_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PageRequest createPageRequestAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.Direction.ASC, "id");
    }

    public static PageRequest createPageRequestDesc(String sortBy, int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(sortBy).descending());
    }
}