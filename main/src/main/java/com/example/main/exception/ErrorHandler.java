package com.example.main.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({EventStatusException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDateEventError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }


    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({ConflictEntityException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictEntityError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("BAD_REQUEST",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({UnavailableException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUnavailableError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info(e.getMessage());
        return new ErrorResponse("INTERNAL_SERVER_ERROR",
                "INTERNAL_SERVER_ERROR",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            NumberFormatException.class,
            MethodArgumentNotValidException.class,
            EventDateException.class})
    public ErrorResponse handleBadRequest(final Throwable e) {
        log.info(e.getMessage());
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorResponse {
        String status;
        String reason;
        String message;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp;
    }
}

