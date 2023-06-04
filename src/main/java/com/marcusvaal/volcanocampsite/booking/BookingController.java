package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.BookingDTO;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

//    private Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PutMapping("/book")
    public BookingDTO bookDuration(@Valid @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Booking response = bookingService.bookDuration(booking);
        return bookingMapper.toDto(response);
    }

    @DeleteMapping("/cancel/{id}")
    public void cancelBookingById(@Valid @PathVariable("id") Long id) {
        bookingService.cancelBookingById(id);
    }

    @GetMapping("/{id}")
    public Optional<BookingDTO> camperById(@PathVariable("id") @Valid @NotNull Long id) {
        return bookingService.findById(id).map(bookingMapper::toDto);
    }

    @GetMapping
    public Stream<BookingDTO> allBookings() {
        return bookingService.allBookings().map(bookingMapper::toDto);
    }
}
