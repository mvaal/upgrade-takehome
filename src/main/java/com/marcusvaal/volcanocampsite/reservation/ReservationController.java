package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.booking.BookingRepository;
import com.marcusvaal.volcanocampsite.booking.BookingService;
import com.marcusvaal.volcanocampsite.model.DateDuration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public Optional<Reservation> reservationById(@Valid @PathVariable("id") Long id) {
        return reservationService.reservationById(id);
    }

//    @GetMapping("/{id}")
//    public List<Reservation> reservationsByBookingId(@Valid @PathVariable("id") Long id) {
//        return reservationService.reservationsByBookingId(id);
//    }

    @GetMapping
    public List<Reservation> allReservations() {
        return reservationService.allReservations();
    }
}
