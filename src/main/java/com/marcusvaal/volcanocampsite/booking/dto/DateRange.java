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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class DateRange {
    @JsonFormat(pattern = "MM/dd/yyyy")
    @NotInThePast(message = "The campsite can not be reserved in the past.")
    @MinAfterToday(days = 1, message = "The campsite can be reserved minimum 1 day(s) ahead of arrival.")
    @MaxAfterToday(days = 0, months = 1, message = "The campsite can be reserved up to 1 month in advance.")
    private LocalDate startDate;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @NotInThePast(message = "The campsite can not be reserved in the past.")
    @MinAfterToday(days = 1, message = "The campsite can be reserved minimum 1 day(s) ahead of arrival.")
    @MaxAfterToday(days = 0, months = 1, message = "The campsite can be reserved up to 1 month in advance.")
    private LocalDate endDate;

    @Min(value = 1, message = "The campsite can be reserved for min 1 day.")
    @Max(value = 3, message = "The campsite can be reserved for max 3 days.")
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
        return Optional.ofNullable(durationDays).orElseGet(() -> ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1);
    }

    public LocalDate getEndDate() {
        return Optional.ofNullable(endDate).orElseGet(() -> startDate.plusDays(durationDays - 1));
    }

    @JsonIgnore
    public Stream<LocalDate> dateStream() {
        return LongStream.range(0, this.getDurationDays())
                .mapToObj(i -> this.startDate.plusDays(i));
    }

    @AssertTrue(message = "Only specify endDate or duration, but not both")
    @JsonIgnore
    public boolean isOneOrOther() {
        return (endDate != null && durationDays == null) || (endDate == null && durationDays != null);
    }

    @AssertTrue(message = "startDate must be the same or before endDate")
    @JsonIgnore
    public boolean isStartDateBeforeEndDate() {
        return endDate == null || !endDate.isBefore(startDate);
    }

    @AssertTrue(message = "The campsite can be reserved for max 3 days")
    @JsonIgnore
    public boolean isMaxThreeDays() {
        return this.getDurationDays() >= 1 && this.getDurationDays() <= 3;
    }
}
