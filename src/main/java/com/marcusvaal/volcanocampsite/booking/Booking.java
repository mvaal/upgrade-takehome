package com.marcusvaal.volcanocampsite.booking;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonManagedReference
    private List<Reservation> reservations;
}