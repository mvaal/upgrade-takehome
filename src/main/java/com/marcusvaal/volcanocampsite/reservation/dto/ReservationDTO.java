package com.marcusvaal.volcanocampsite.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Reservation DTO
 * @param date Reservation Date
 * @param cost Reservation Cost (always 0)
 */
public record ReservationDTO(@NotNull LocalDate date, @NotNull BigDecimal cost) {
}
