package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.model.DateDuration;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import com.marcusvaal.volcanocampsite.reservation.ReservationRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Observed(name = "booking.book.duration", contextualName = "BookingService.bookDuration")
    public Booking bookDuration(@Valid DateDuration dateDuration) {
        Booking booking = Booking.builder().build();
        List<Reservation> reservations = IntStream.range(0, dateDuration.duration())
                .mapToObj(i -> Reservation.builder()
                        .date(dateDuration.startDate().plusDays(i))
                        .booking(booking)
                        .build()).toList();
        booking.setReservations(reservations);

        return bookingRepository.save(booking);
    }

    @Observed(name = "booking.cancel.booking", contextualName = "BookingService.cancelBookingById")
    public void cancelBookingById(@Valid Long id) {
        bookingRepository.deleteById(id);
    }
}
