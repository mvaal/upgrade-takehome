package com.marcusvaal.volcanocampsite.booking.dto;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Booking Data Transfer Object
 *
 * @param bookingId Database Booking ID
 * @param camper    Camper
 * @param dateRange Date Range
 */
public record BookingDTO(
        @NotNull
        Long bookingId,
        @NotNull(message = "User information is required")
        @Valid
        CamperDTO camper,
        @NotNull(message = "Must provide date range")
        @Valid
        StrictDateRange dateRange
) {
}
