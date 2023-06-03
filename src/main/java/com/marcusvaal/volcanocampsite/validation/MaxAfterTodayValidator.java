package com.marcusvaal.volcanocampsite.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MaxAfterTodayValidator implements ConstraintValidator<MaxAfterToday, LocalDate> {
    private long days;
    private long months;

    @Override
    public void initialize(MaxAfterToday constraintAnnotation) {
        days = constraintAnnotation.days();
        months = constraintAnnotation.months();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        // Since this is a less than instead of less than or equals, subtract a day
        return LocalDate.now().plusDays(days).plusMonths(months).isAfter(date);
    }
}