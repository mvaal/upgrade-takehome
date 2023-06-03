package com.marcusvaal.volcanocampsite.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class MinAfterTodayValidator implements ConstraintValidator<MinAfterToday, LocalDate> {
    private long days;

    @Override
    public void initialize(MinAfterToday constraintAnnotation) {
        days = constraintAnnotation.days();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null || !LocalDate.now().plusDays(days).isAfter(date);
    }
}