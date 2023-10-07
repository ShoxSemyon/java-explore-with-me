package com.example.main.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConflictEntityException extends RuntimeException {
    public ConflictEntityException(String msg) {
        super(msg);
    }
}
