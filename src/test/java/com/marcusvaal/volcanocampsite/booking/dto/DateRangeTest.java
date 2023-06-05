package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("DateRange")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DateRangeTest {
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_have_no_violations_for_valid_date_range() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        dateRange = new DateRange(LocalDate.now().plusDays(1), startDate.plusDays(2));
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
    }

    @Test
    public void should_throw_invalidformatexception_for_invalid_start_date() {
        final String badDateRangeStr = "{\"startDate\":\"6/04/2023\",\"endDate\":\"06/04/2023\"}";
        InvalidFormatException thrown = assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(badDateRangeStr, DateRange.class));
        assertThat(thrown.getValue(), is("6/04/2023"));
        final String goodDateRangeStr = "{\"startDate\":\"06/04/2023\",\"endDate\":\"06/04/2023\"}";
        assertDoesNotThrow(() -> objectMapper.readValue(goodDateRangeStr, DateRange.class));
    }

    @Test
    public void should_have_violation_for_null_start_date() {
        DateRange dateRange = new DateRange(null, null);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(4));
    }

    @Test
    public void should_have_violation_for_start_and_end_date_in_the_past() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(2));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    public void should_have_violation_for_start_date_not_min_after() {
        LocalDate startDate = LocalDate.now();
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(2));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_violation_for_start_date_not_max_after() {
        LocalDate startDate = LocalDate.now().plusMonths(1);
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(2));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    public void should_throw_invalidformatexception_for_invalid_end_date() {
        String dateRangeStr = "{\"startDate\":\"06/04/2023\",\"endDate\":\"06/5/2023\"}";
        InvalidFormatException thrown = assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(dateRangeStr, DateRange.class));
        assertThat(thrown.getValue(), is("06/5/2023"));
    }


    @Test
    public void should_not_allow_end_date_before_start_date() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.minusDays(1));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(3));
    }

    @Test
    public void should_not_allow_duration_less_than_1_or_greater_than_3() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(3));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
        dateRange = new DateRange(startDate, startDate.plusDays(1));
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        dateRange = new DateRange(startDate, startDate.plusDays(2));
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
    }

    @Test
    public void should_return_the_correct_duration() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(2));
        assertThat(dateRange.getDurationDays(), is(3L));
    }
}
