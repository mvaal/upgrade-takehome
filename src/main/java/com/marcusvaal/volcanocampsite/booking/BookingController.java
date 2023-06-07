package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.BookingDTO;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

/**
 * Booking Controller
 */
@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking", description = "Campsite Bookings")
@RequiredArgsConstructor
public class BookingController {
    private final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    /**
     * Makes a booking
     * @param bookingRequest booking request
     * @return booking DTO
     */
    @PostMapping("/booking")
    @Operation(summary = "Book a reservation", description = "Book a reservation for the campsite")
    public BookingDTO book(@Valid @RequestBody BookingRequest bookingRequest) {
        logger.debug("Request - Booking with request: {}", bookingRequest);
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Booking response = bookingService.bookDuration(booking);
        return bookingMapper.toDto(response);
    }

    /**
     * Update a booking
     * @param id Booking id
     * @param dateRange date range
     * @return Booking DTO
     */
    @PutMapping("/booking/{id}")
    @Operation(summary = "Update a reservation", description = "Update a reservation based on Booking ID")
    public ResponseEntity<BookingDTO> updateDuration(@NotNull @PathVariable("id") Long id, @RequestBody @Valid StrictDateRange dateRange) {
        logger.debug("Request - Update Booking ID {} with date range: {}", id, dateRange);
        return bookingService.updateDuration(id, dateRange)
                .map(bookingMapper::toDto)
                .map(booking -> new ResponseEntity<>(booking, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a booking
     * @param id booking id
     * @return ok if found, not found if not
     */
    @DeleteMapping("/booking/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancel a reservation based on Booking ID")
    public ResponseEntity<Object> cancelBookingById(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Delete Booking: {}", id);
        if (!bookingService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get booking by booking ID
     * @param id Booking ID
     * @return Booking ID
     */
    @GetMapping("/booking/{id}")
    @Operation(summary = "Get Booking By Booking ID", description = "Get booking by Booking ID")
    public ResponseEntity<BookingDTO> bookingById(@PathVariable("id") @Valid @NotNull Long id) {
        logger.debug("Request - Booking with ID: {}", id);
        return bookingService.findById(id)
                .map(bookingMapper::toDto)
                .map(bookingDto -> new ResponseEntity<>(bookingDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get all bookings
     * @return Stream of Bookings
     */
    @GetMapping
    @Operation(summary = "Get all Bookings", description = "Get all Bookings - Utility")
    public Stream<BookingDTO> allBookings() {
        logger.debug("Request - All Bookings");
        return bookingService.allBookings().map(bookingMapper::toDto);
    }
}
