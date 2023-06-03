package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.model.DateDuration;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import com.marcusvaal.volcanocampsite.reservation.ReservationController;
import com.marcusvaal.volcanocampsite.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final ReservationService reservationService;

    private Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PutMapping("/book")
    public Booking bookDuration(@Valid @RequestBody DateDuration dateDuration) {
        return bookingService.bookDuration(dateDuration);
    }

    @GetMapping("/reservations/{id}")
    public List<Reservation> reservationsByBookingId(@Valid @PathVariable("id") Long id) {
        return reservationService.reservationsByBookingId(id);
    }

    @DeleteMapping("/cancel/{id}")
    public void cancelBookingById(@Valid @PathVariable("id") Long id) {
        bookingService.cancelBookingById(id);
    }
}
