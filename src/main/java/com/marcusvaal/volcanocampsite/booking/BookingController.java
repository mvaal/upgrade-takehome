package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.BookingDTO;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking", description = "Campsite Bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/booking")
    public BookingDTO book(@Valid @RequestBody BookingRequest bookingRequest) {
        logger.debug("Request - Booking with request: {}", bookingRequest);
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Booking response = bookingService.bookDuration(booking);
        return bookingMapper.toDto(response);
    }

    @PutMapping("/booking/{id}")
    public ResponseEntity<BookingDTO> updateDuration(@NotNull @PathVariable("id") Long id, @RequestBody @Valid StrictDateRange dateRange) {
        logger.debug("Request - Update Booking ID {} with date range: {}", id, dateRange);
        return bookingService.updateDuration(id, dateRange)
                .map(bookingMapper::toDto)
                .map(booking -> new ResponseEntity<>(booking, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<Object> cancelBookingById(@Valid @PathVariable("id") Long id) {
        logger.debug("Request - Delete Booking: {}", id);
        if (!bookingService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/booking/{id}")
    @Operation(summary = "Booking By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    public ResponseEntity<BookingDTO> bookingById(@PathVariable("id") @Valid @NotNull Long id) {
        logger.debug("Request - Booking with ID: {}", id);
        return bookingService.findById(id)
                .map(bookingMapper::toDto)
                .map(bookingDto -> new ResponseEntity<>(bookingDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Stream<BookingDTO> allBookings() {
        logger.debug("Request - All Bookings");
        return bookingService.allBookings().map(bookingMapper::toDto);
    }
}
