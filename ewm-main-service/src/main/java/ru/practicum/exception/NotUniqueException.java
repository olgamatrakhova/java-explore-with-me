package ru.practicum.exception;

public class NotUniqueException extends RuntimeException {
    public NotUniqueException(final String massage) {
        super(massage);
    }
}