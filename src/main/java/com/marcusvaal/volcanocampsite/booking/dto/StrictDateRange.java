package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcusvaal.volcanocampsite.validation.MaxAfterToday;
import com.marcusvaal.volcanocampsite.validation.MinAfterToday;
import com.marcusvaal.volcanocampsite.validation.NotInThePast;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Date range for booking request
 * @param startDate Start date
 * @param endDate End date
 */
public record StrictDateRange(
        @JsonFormat(pattern = "MM/dd/yyyy")
        @NotNull
        @NotInThePast(message = "The startDate can not be in the past.")
        @MinAfterToday(days = 1, message = "The startDate can be a minimum 1 day(s) ahead of arrival.")
        @MaxAfterToday(days = 0, months = 1, message = "The startDate can be up to 1 month in advance.")
        LocalDate startDate,

        @JsonFormat(pattern = "MM/dd/yyyy")
        @NotNull
        @NotInThePast(message = "The startDate can not be in the past.")
        @MinAfterToday(days = 1, message = "The startDate can be a minimum 1 day(s) ahead of arrival.")
        @MaxAfterToday(days = 0, months = 1, message = "The startDate can be up to 1 month in advance.")
        LocalDate endDate
) {

    /**
     * Duration in days
     * @return duration in days
     */
    @JsonIgnore
    public Long getDurationDays() {
        return ChronoUnit.DAYS.between(this.startDate, endDate) + 1;
    }

    @JsonIgnore
    public Stream<LocalDate> dateStream() {
        return LongStream.range(0, this.getDurationDays())
                .mapToObj(this.startDate::plusDays);
    }

    /**
     * Date Range validation for to compare start and end dates
     * null check is required here as it can be set to null during validation
     *
     * @return true if start date is before or equal to end date
     */
    @AssertTrue(message = "startDate must be the same or before endDate")
    @JsonIgnore
    public boolean isStartDateBeforeEndDate() {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    /**
     * Check if the dates duration is correct
     * @return true if duration is between 1 and 3
     */
    @AssertTrue(message = "The campsite can be reserved for min 1 day and max 3 days")
    @JsonIgnore
    public boolean isCorrectDuration() {
        return startDate != null && endDate != null && this.getDurationDays() >= 1 && this.getDurationDays() <= 3;
    }
}
