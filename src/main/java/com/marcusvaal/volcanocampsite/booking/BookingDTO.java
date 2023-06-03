package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.camper.CamperDTO;
import com.marcusvaal.volcanocampsite.dto.DateRange;
import jakarta.validation.constraints.NotNull;

public record BookingDTO(
        @NotNull(message = "User information is required.")
        CamperDTO camper,
        @NotNull(message = "Must provide date range")
        DateRange dateRange
) {
}
