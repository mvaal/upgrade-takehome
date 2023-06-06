package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.booking.BookingService;
import com.marcusvaal.volcanocampsite.booking.dto.OpenDateRange;
import com.marcusvaal.volcanocampsite.reservation.dto.ReservationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservations", description = "Reservation Dates")
@RequiredArgsConstructor
public class ReservationController {
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;
    private final BookingService bookingService;
    private final ReservationMapper reservationMapper;

    @GetMapping("/reservation/{id}")
    @Operation(summary = "Get Reservation By Reservation ID", description = "Get Reservation by Reservation ID - Utility")
    public ResponseEntity<ReservationDTO> reservationById(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Reservation with ID: {}", id);
        return reservationService.reservationById(id)
                .map(reservationMapper::toDto)
                .map(reservationDto -> new ResponseEntity<>(reservationDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/booking/{id}")
    @Operation(summary = "Get Reservation By Booking ID", description = "Get Reservation by Booking ID")
    public ResponseEntity<Stream<ReservationDTO>> reservationsByBookingId(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Reservation with Booking ID: {}", id);
        if (!bookingService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Stream<ReservationDTO> stream = reservationService.reservationsByBookingId(id)
                .map(reservationMapper::toDto);
        return new ResponseEntity<>(stream, HttpStatus.OK);
    }

    @PostMapping("/available")
    @Operation(summary = "Get Reservations Available by Date Range", description = "Get Reservations available by Date Range or month span")
    public Stream<ReservationDTO> availableReservations(@Valid @RequestBody OpenDateRange dateRange) {
        logger.debug("Request - Reservation with date range: {}", dateRange);
        return reservationService.availableReservations(dateRange)
                .map(reservationMapper::toDto);
    }

    @GetMapping
    @Operation(summary = "Get all Reservations", description = "Get all Reservations - Utility")
    public Stream<ReservationDTO> allReservations() {
        logger.debug("Request - All Reservations");
        return reservationService.allReservations().map(reservationMapper::toDto);
    }
}
