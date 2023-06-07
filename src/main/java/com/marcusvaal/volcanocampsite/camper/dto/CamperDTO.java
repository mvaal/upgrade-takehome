package com.marcusvaal.volcanocampsite.camper.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * Camper Record
 * @param email Camper email
 * @param fullName Camper full name
 */
public record CamperDTO(@NotNull @Email String email, @NotNull String fullName) {
}
