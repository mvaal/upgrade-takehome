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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "camper_id")
    @JsonBackReference
    private Camper camper;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonManagedReference
    @Builder.Default
    private List<Reservation> reservations = Collections.emptyList();

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", camper=" + camper.getId() +
                ", reservations=" + reservations +
                '}';
    }
}