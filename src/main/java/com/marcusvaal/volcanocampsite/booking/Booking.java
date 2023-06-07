package com.marcusvaal.volcanocampsite.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import com.marcusvaal.volcanocampsite.camper.Camper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Booking Entity
 */
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "camper_id")
    @JsonBackReference
    private Camper camper;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "booking", orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Reservation> reservations = Collections.emptySet();

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", camper=" + camper.getId() +
                ", reservations=" + reservations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(camper, booking.camper) && Objects.equals(reservations, booking.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(camper, reservations);
    }
}