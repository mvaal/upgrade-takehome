package com.marcusvaal.volcanocampsite.booking.dto;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for a booking request
 *
 * @param camper    Camper
 * @param dateRange Date range
 */
public record BookingRequest(
        @NotNull(message = "User information is required.")
        @Valid
        CamperDTO camper,
        @NotNull(message = "Must provide date range")
        @Valid
        StrictDateRange dateRange
) {
}
