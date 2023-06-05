package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.reservation.dto.ReservationDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservations", description = "Reservation Dates")
@RequiredArgsConstructor
public class ReservationController {
    private Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping("/{id}")
    public Optional<ReservationDTO> reservationById(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Reservation with ID: {}", id);
        return reservationService.reservationById(id).map(reservationMapper::toDto);
    }

    @GetMapping("/booking/{id}")
    public Stream<ReservationDTO> reservationsByBookingId(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Reservation with Booking ID: {}", id);
        return reservationService.reservationsByBookingId(id).map(reservationMapper::toDto);
    }

    @GetMapping
    public Stream<ReservationDTO> allReservations() {
        logger.debug("Request - All Reservations");
        return reservationService.allReservations().map(reservationMapper::toDto);
    }
}
