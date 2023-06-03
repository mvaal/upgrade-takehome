package com.marcusvaal.volcanocampsite.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marcusvaal.volcanocampsite.validation.MaxAfterToday;
import com.marcusvaal.volcanocampsite.validation.MinAfterToday;
import com.marcusvaal.volcanocampsite.validation.NotInThePast;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DateDuration(
        @NotNull
        @JsonFormat(pattern = "MM/dd/yyyy")
        @NotInThePast(message = "The campsite can not be reserved in the past.")
        @MinAfterToday(days = 1, message = "The campsite can be reserved minimum 1 day(s) ahead of arrival.")
        @MaxAfterToday(days = 0, months = 1, message = "The campsite can be reserved up to 1 month in advance.")
        LocalDate startDate,
        @Min(value = 1, message = "The campsite can be reserved for min 1 day.")
        @Max(value = 3, message = "The campsite can be reserved for max 3 days.")
        @NotNull(message = "A duration between 1 and 3 days is required.")
        Integer duration
) {
    public LocalDate endDate() {
        return startDate.plusDays(duration);
    }

    public DateRange getDateRange() {
        return new DateRange(startDate, endDate());
    }
}
