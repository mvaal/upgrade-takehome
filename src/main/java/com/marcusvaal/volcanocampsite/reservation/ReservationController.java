package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.model.DateDuration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public Optional<Reservation> reservationById(@Valid @PathVariable("id") Long id) {
        return reservationService.reservationById(id);
    }

    @GetMapping
    public List<Reservation> allReservations() {
        return reservationService.allReservations();
    }

    @PutMapping("/duration")
    public List<Reservation> reserveDuration(@Valid @RequestBody DateDuration dateDuration) {
        return reservationService.reserveDuration(dateDuration);
    }

    @DeleteMapping("/cancel/{id}")
    public void cancelReservationById(@Valid @PathVariable("id") Long id) {
        reservationService.cancelReservationById(id);
    }
}
