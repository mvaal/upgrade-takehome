package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.booking.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @NotNull
    @Column(unique=true)
    private LocalDate date;

    @NotNull
    @Min(value = 0, message = "The campsite should be free for all.")
    @Max(value = 0, message = "The campsite should be free for all.")
    @Builder.Default
    private BigDecimal cost = BigDecimal.valueOf(0);
}