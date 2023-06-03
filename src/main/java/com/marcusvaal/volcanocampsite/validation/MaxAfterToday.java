package com.marcusvaal.volcanocampsite.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MaxAfterTodayValidator.class)
@Documented
public @interface MaxAfterToday {
    String message() default "{message.key}";

    long days();

    long months();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
