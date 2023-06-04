package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.BookingDTO;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking", description = "Campsite Bookings")
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
    @Operation(summary = "Booking By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    public Optional<BookingDTO> camperById(@PathVariable("id") @Valid @NotNull Long id) {
        return bookingService.findById(id).map(bookingMapper::toDto);
    }

    @GetMapping
    public Stream<BookingDTO> allBookings() {
        return bookingService.allBookings().map(bookingMapper::toDto);
    }
}
