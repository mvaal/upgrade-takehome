package com.marcusvaal.volcanocampsite.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationDTO(@NotNull LocalDate date, @NotNull BigDecimal cost) {
}
