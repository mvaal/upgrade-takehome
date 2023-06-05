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

    @JsonProperty
    public Long getDurationDays() {
        return ChronoUnit.DAYS.between(this.startDate, endDate()) + 1;
    }

    @JsonIgnore
    public Stream<LocalDate> dateStream() {
        return LongStream.range(0, this.getDurationDays())
                .mapToObj(this.startDate::plusDays);
    }

    @AssertTrue(message = "startDate must be the same or before endDate")
    @JsonIgnore
    public boolean isStartDateBeforeEndDate() {
        return startDate != null && !endDate().isBefore(startDate);
    }
}
