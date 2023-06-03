package com.marcusvaal.volcanocampsite.web.controller;

import com.marcusvaal.volcanocampsite.web.model.DateDuration;
import com.marcusvaal.volcanocampsite.web.model.Reservation;
import com.marcusvaal.volcanocampsite.web.repository.ReservationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationV1Controller {
    private final ReservationRepository reservationRepository;

    @GetMapping("/{id}")
    public Optional<Reservation> reservationById(@Valid @PathVariable("id") Long id) {
        return reservationRepository.findById(id);
    }

    @GetMapping
    public List<Reservation> all() {
        return reservationRepository.findAll();
    }

    @PutMapping("/duration")
    public Reservation reserveDuration(@Valid @RequestBody DateDuration dateDuration) {
        Reservation reservation = Reservation.builder()
                .startDate(dateDuration.startDate().atStartOfDay())
                .endDate(dateDuration.endDate().atStartOfDay())
                .build();
        return reservationRepository.save(reservation);
    }
}
