package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.camper.Camper;
import com.marcusvaal.volcanocampsite.camper.CamperMapper;
import com.marcusvaal.volcanocampsite.camper.CamperRepository;
import com.marcusvaal.volcanocampsite.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.dto.BookingResponse;
import com.marcusvaal.volcanocampsite.dto.BookingResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingResponseMapper bookingResponseMapper;
    private final BookingMapper bookingMapper;

//    private Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PutMapping("/book")
    public BookingResponse bookDuration(@Valid @RequestBody BookingDTO bookingDto) {
        Booking booking = bookingMapper.toBooking(bookingDto);
        Booking response = bookingService.bookDuration(booking);
        return bookingResponseMapper.toDto(response);
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
        List<Booking> bookings = bookingService.allBookings();
        return bookingService.allBookings().stream().map(bookingMapper::toDto);
    }
}
