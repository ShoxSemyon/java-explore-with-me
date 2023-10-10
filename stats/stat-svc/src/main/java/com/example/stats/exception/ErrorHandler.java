package com.example.stats.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            NumberFormatException.class,
            MethodArgumentNotValidException.class,
            DateNotValidException.class})
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
