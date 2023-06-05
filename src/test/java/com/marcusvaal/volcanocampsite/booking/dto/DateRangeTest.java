package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.format.DateTimeFormatter;
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
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(1), 1L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(0));
        dateRange = new DateRange(LocalDate.now().plusDays(1), 3L);
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
        DateRange dateRange = new DateRange(null, 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_violation_for_start_date_in_the_past() {
        DateRange dateRange = new DateRange(LocalDate.now().minusDays(1), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    public void should_have_violation_for_start_date_not_min_after() {
        DateRange dateRange = new DateRange(LocalDate.now(), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_violation_for_start_date_not_max_after(){
        DateRange dateRange = new DateRange(LocalDate.now().plusMonths(1), 3L);
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_throw_invalidformatexception_for_invalid_end_date() {
        String dateRangeStr = "{\"startDate\":\"06/04/2023\",\"endDate\":\"06/5/2023\"}";
        InvalidFormatException thrown = assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(dateRangeStr, DateRange.class));
        assertThat(thrown.getValue(), is("06/5/2023"));
    }

    @Test
    public void should_not_allow_end_date_and_duration_to_both_be_set_or_null() throws JsonProcessingException {
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
    public void should_not_allow_end_date_before_start_date() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, startDate.minusDays(1));
        Set<ConstraintViolation<DateRange>> violations = validator.validate(dateRange);
        assertThat(violations, hasSize(2));
    }

    @Test
    public void should_not_allow_duration_less_than_1_or_greater_than_3() {
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
    public void should_return_the_correct_duration() {
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(1), 3L);
        assertThat(dateRange.getDurationDays(), is(3L));
        final DateRange badDateRange = new DateRange(LocalDate.now().plusDays(1), (LocalDate) null);
        assertThrows(RuntimeException.class, badDateRange::getEndDate);
    }

    @Test
    public void should_return_the_correct_end_date() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        DateRange dateRange = new DateRange(startDate, 3L);
        assertThat(dateRange.getEndDate(), is(startDate.plusDays(dateRange.getDurationDays() - 1)));
        final DateRange badDateRange = new DateRange(LocalDate.now().plusDays(1), (Long) null);
        assertThrows(RuntimeException.class, badDateRange::getEndDate);
    }
}
