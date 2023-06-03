package com.marcusvaal.volcanocampsite.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class Reservation {
    @Id
    @GeneratedValue
    private Long bookingIdentifier;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    @Min(value = 0, message = "The campsite should be free for all.")
    @Max(value = 0, message = "The campsite should be free for all.")
    @Builder.Default
    private BigDecimal cost = BigDecimal.valueOf(0);
}