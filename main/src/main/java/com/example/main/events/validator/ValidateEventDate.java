package com.example.main.events.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = {EventDateValidator.class})
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEventDate {
    String message() default "ZZZ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}