package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    @Slf4j
    @RestControllerAdvice
    public static class ErrorHandler {
        @ExceptionHandler({MethodArgumentNotValidException.class, MissingRequestHeaderException.class,
                ConstraintViolationException.class, MissingServletRequestParameterException.class,
                WrongTimeException.class})
        public ResponseEntity<ErrorResponse> badRequest(final Exception e) {
            log.error("Bad request: Description: {}, Status: {}", e.getMessage(), HttpStatus.BAD_REQUEST.toString());
            return handleException(e, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler
        public ResponseEntity<ErrorResponse> internalServerError(final Exception e) {
            log.error("Description: {}, Status: {}", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        private ResponseEntity<ErrorResponse> handleException(final Exception e, HttpStatus status) {
            String errorMessage = status.is4xxClientError() ? "Incorrectly made request." : "Internal Server Error";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String errorStatus = status.name();
            String errorDescription = e.getMessage();
            log.error("{} - Status: {}, Description: {}, Timestamp: {}",
                    errorMessage, errorStatus, errorDescription, timestamp);
            return new ResponseEntity<>(new ErrorResponse(errorStatus, errorDescription, errorMessage, timestamp), status);
        }
    }
}