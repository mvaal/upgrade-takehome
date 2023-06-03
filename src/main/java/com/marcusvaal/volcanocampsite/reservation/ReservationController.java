package com.marcusvaal.volcanocampsite.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
//    private Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping("/{id}")
    public Optional<ReservationDTO> reservationById(@Valid @PathVariable("id") Long id) {
        return reservationService.reservationById(id).map(reservationMapper::toDto);
    }

    @GetMapping("/booking/{id}")
    public Stream<ReservationDTO> reservationsByBookingId(@Valid @PathVariable("id") Long id) {
        return reservationService.reservationsByBookingId(id).map(reservationMapper::toDto);
    }

    @GetMapping
    public Stream<ReservationDTO> allReservations() {
        return reservationService.allReservations().map(reservationMapper::toDto);
    }
}
