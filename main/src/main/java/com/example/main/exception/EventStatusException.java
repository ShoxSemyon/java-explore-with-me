package com.example.main.exception;

public class EventStatusException extends RuntimeException {

    public EventStatusException() {
    }

    public EventStatusException(String message) {
        super(message);
    }
}
