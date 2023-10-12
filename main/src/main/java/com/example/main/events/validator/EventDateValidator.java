package com.example.main.events.validator;

import com.example.main.events.validator.ValidateEventDate;
import com.example.main.exception.EventDateException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateValidator implements ConstraintValidator<ValidateEventDate, LocalDateTime> {
    @Override
    public void initialize(ValidateEventDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return true;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (s.isBefore(LocalDateTime.now().plusHours(2)))
            throw new EventDateException("Field: eventDate. Error: должно " +
                    "содержать дату, которая еще не наступила. Value: " + dateTimeFormatter.format(s));
        return true;
    }
}
