package ru.practicum.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(final String massage) {
        super(massage);
    }
}