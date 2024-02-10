package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static ru.practicum.utils.Utils.FORMATTER;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            BadRequestException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final Exception e) {
        log.error("{} - Status: {}, Description: {}, Timestamp: {}", "Bad Request", HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now());
        return ApiError.builder().status(HttpStatus.BAD_REQUEST.name())
                .reason("Bad Request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER)).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleInternalServerError(final Exception e) {
        log.error("{} - Status: {}, Description: {}, Timestamp: {}",
                "SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), LocalDateTime.now());
        return ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal Server Error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER)).build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class, NotUniqueException.class})
    public ApiError handleConflict(final Exception e) {
        log.error("{} - Status: {}, Description: {}, Timestamp: {}",
                "CONFLICT", HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
        return ApiError.builder().status(HttpStatus.CONFLICT.name())
                .reason("Data Integrity constraint violation occurred")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER)).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class, IllegalArgumentException.class,
            NoSuchElementException.class, NotFoundException.class})
    public ApiError handleNotFound(final RuntimeException e) {
        log.error("{} - Status: {}, Description: {}, Timestamp: {}",
                "NOT FOUND", HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
        return ApiError.builder().status(HttpStatus.NOT_FOUND.name())
                .reason("Not Found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER)).build();
    }
}