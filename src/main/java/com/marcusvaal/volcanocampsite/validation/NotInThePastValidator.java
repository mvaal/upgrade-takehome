package com.marcusvaal.volcanocampsite.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotInThePastValidator implements ConstraintValidator<NotInThePast, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return !LocalDate.now().isAfter(date);
    }
}