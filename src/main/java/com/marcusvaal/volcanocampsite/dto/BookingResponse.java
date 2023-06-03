package com.marcusvaal.volcanocampsite.dto;

import com.marcusvaal.volcanocampsite.camper.CamperDTO;
import jakarta.validation.constraints.NotNull;

public record BookingResponse(
        @NotNull(message = "User information is required.")
        CamperDTO camper,
        @NotNull(message = "Must provide date range")
        DateRange dateRange
) {

}
