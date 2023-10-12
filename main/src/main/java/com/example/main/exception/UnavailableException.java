package com.example.main.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnavailableException extends RuntimeException {
    public UnavailableException(String message) {
        super(message);
    }
}
