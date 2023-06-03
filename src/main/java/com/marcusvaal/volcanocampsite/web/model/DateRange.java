package com.marcusvaal.volcanocampsite.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DateRange(
        @NotNull
        @JsonFormat(pattern = "MM/dd/yyyy")
        LocalDate startDate,
        LocalDate endDate
) {
}
