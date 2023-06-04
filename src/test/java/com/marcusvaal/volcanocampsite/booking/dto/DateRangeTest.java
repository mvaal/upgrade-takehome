package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DateRangeTest {
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test valid DateRange")
    public void testValidDateRange() {
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(1), 1L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        dateRange = new DateRange(LocalDate.now().plusDays(1), 3L);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
    }

    @Test
    @DisplayName("Test improper start date format throws exception")
    public void testStartDateFormatValidation() {
        final String badDateRangeStr = "{\"startDate\":\"6/04/2023\",\"endDate\":\"06/04/2023\"}";
        InvalidFormatException thrown = assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(badDateRangeStr, DateRange.class));
        assertThat(thrown.getValue(), is("6/04/2023"));
        final String goodDateRangeStr = "{\"startDate\":\"06/04/2023\",\"endDate\":\"06/04/2023\"}";
        assertDoesNotThrow(() -> objectMapper.readValue(goodDateRangeStr, DateRange.class));
    }

    @Test
    @DisplayName("Test start date not null")
    public void testStartDateNotNullValidation() {
        DateRange dateRange = new DateRange(null, 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    @DisplayName("Test start date not in the past")
    public void testStartDateNotInThePastValidation() {
        DateRange dateRange = new DateRange(LocalDate.now().minusDays(1), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    @DisplayName("Test start date is minimum of 1 day later")
    public void testStartDateMinAfterTodayValidation() {
        DateRange dateRange = new DateRange(LocalDate.now(), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    @DisplayName("Test start date is not more than 1 month later")
    public void testStartDateMaxAfterTodayValidation() {
        DateRange dateRange = new DateRange(LocalDate.now().plusMonths(1), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    @DisplayName("Test improper end date format throws exception")
    public void testEndDateFormatValidation() {
        String dateRangeStr = "{\"startDate\":\"06/04/2023\",\"endDate\":\"06/5/2023\"}";
        InvalidFormatException thrown = assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(dateRangeStr, DateRange.class));
        assertThat(thrown.getValue(), is("06/5/2023"));
    }

    @Test
    @DisplayName("Test end date is minimum of 1 day later")
    public void testEndDateMinAfterTodayValidation() {
        LocalDate endDate = LocalDate.now();
        DateRange dateRange = new DateRange(endDate.minusDays(1), endDate);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    @DisplayName("Test DateRange has endDate or duration but not neither or both")
    public void testIsOneOrOtherValidation() throws JsonProcessingException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String startDateStr = formatter.format(startDate);
        String endDateStr = formatter.format(endDate);
        long duration = 3;
        final String bothDateRange = "{\"startDate\":\"" + startDateStr + "\",\"endDate\":\"" + endDateStr + "\",\"duration\":" + duration + "}";
        DateRange dateRange = objectMapper.readValue(bothDateRange, DateRange.class);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
        final String noDateRange = "{\"startDate\":\"" + startDateStr + "\"}";
        dateRange = objectMapper.readValue(noDateRange, DateRange.class);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
        final String endDateRange = "{\"startDate\":\"" + startDateStr + "\",\"endDate\":\"" + endDateStr + "\"}";
        dateRange = objectMapper.readValue(endDateRange, DateRange.class);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        final String durationDateRange = "{\"startDate\":\"" + startDateStr + "\",\"duration\":" + duration + "}";
        dateRange = objectMapper.readValue(durationDateRange, DateRange.class);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
    }

    @Test
    @DisplayName("Test end date is not before start date")
    public void testIsStartDateBeforeEndDateValidation() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.minusDays(1));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    @DisplayName("Test duration is not less than 1")
    public void testIsCorrectDurationValidation() {
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(1), 0L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
        dateRange = new DateRange(LocalDate.now().plusDays(1), 4L);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
        dateRange = new DateRange(LocalDate.now().plusDays(1), 1L);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        dateRange = new DateRange(LocalDate.now().plusDays(1), 3L);
        violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
    }

    @Test
    @DisplayName("Test duration")
    public void testDurationDays() {
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(1), 3L);

    }
}
