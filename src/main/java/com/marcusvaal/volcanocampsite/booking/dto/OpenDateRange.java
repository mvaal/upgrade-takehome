package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Range for availability requests
 *
 * @param startDate Start date
 * @param endDate   (Optional) End date - 1 month after start if not set
 */
public record OpenDateRange(
        @JsonFormat(pattern = "MM/dd/yyyy")
        @NotNull
        LocalDate startDate,

        @JsonFormat(pattern = "MM/dd/yyyy")
        LocalDate endDate
) {

    @Override
    public LocalDate endDate() {
        return Optional.ofNullable(endDate)
                .orElse(this.startDate.plusMonths(1));
    }

    @JsonIgnore
    public Long getDurationDays() {
        return ChronoUnit.DAYS.between(this.startDate, endDate()) + 1;
    }

    /**
     * Stream of dates in the range
     *
     * @return Stream of dates in the range
     */
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
        return startDate != null && !endDate().isBefore(startDate);
    }
}
