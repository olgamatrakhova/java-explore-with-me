package ru.practicum.exception;

public class StatsParseException extends RuntimeException {
    public StatsParseException(final String massage) {
        super(massage);
    }
}