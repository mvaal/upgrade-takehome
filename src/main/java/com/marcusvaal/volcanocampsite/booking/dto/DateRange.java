package com.marcusvaal.volcanocampsite.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcusvaal.volcanocampsite.validation.MaxAfterToday;
import com.marcusvaal.volcanocampsite.validation.MinAfterToday;
import com.marcusvaal.volcanocampsite.validation.NotInThePast;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateRange {
    @JsonFormat(pattern = "MM/dd/yyyy")
    @NotNull
    @NotInThePast(message = "The startDate can not be in the past.")
    @MinAfterToday(days = 1, message = "The startDate can be a minimum 1 day(s) ahead of arrival.")
    @MaxAfterToday(days = 0, months = 1, message = "The startDate can be up to 1 month in advance.")
    private LocalDate startDate;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @JsonProperty(value = "duration")
    private Long durationDays;

    public DateRange(@NotNull LocalDate startDate, @NotNull Long durationDays) {
        this.startDate = startDate;
        this.durationDays = durationDays;
    }

    public DateRange(@NotNull LocalDate startDate,
                     @NotNull LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getDurationDays() {
        return Optional.ofNullable(durationDays)
                .orElseGet(() -> Optional.ofNullable(this.endDate)
                        .map(endDate -> ChronoUnit.DAYS.between(this.startDate, endDate) + 1)
                        .orElse(0L));
    }

    public LocalDate getEndDate() {
        return Optional.ofNullable(endDate).orElseGet(() -> startDate.plusDays(durationDays - 1));
    }

    @JsonIgnore
    public Stream<LocalDate> dateStream() {
        return LongStream.range(0, this.getDurationDays())
                .mapToObj(i -> this.startDate.plusDays(i));
    }

    @AssertTrue(message = "Only specify endDate or duration, but not both or neither")
    @JsonIgnore
    public boolean isOneOrOther() {
        return (endDate != null && durationDays == null) || (endDate == null && durationDays != null);
    }

    @AssertTrue(message = "startDate must be the same or before endDate")
    @JsonIgnore
    public boolean isStartDateBeforeEndDate() {
        return endDate == null || !endDate.isBefore(startDate);
    }

    @AssertTrue(message = "The campsite can be reserved for min 1 day and max 3 days")
    @JsonIgnore
    public boolean isCorrectDuration() {
        return this.getDurationDays() >= 1 && this.getDurationDays() <= 3;
    }
}
