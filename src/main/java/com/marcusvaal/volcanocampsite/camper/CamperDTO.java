package com.marcusvaal.volcanocampsite.camper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CamperDTO(@NotNull @Email String email, @NotNull String fullName) {
}
