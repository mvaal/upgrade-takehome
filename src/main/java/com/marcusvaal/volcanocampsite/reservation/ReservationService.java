package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.booking.Booking;
import com.marcusvaal.volcanocampsite.booking.BookingRepository;
import com.marcusvaal.volcanocampsite.model.DateDuration;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookingRepository bookingRepository;

    public Optional<Reservation> reservationById(@Valid Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public List<Reservation> reserveDuration(@Valid DateDuration dateDuration) {
        Booking booking = Booking.builder().build();
        List<Reservation> reservations = IntStream.range(0, dateDuration.duration())
                .mapToObj(i -> Reservation.builder()
                        .date(dateDuration.startDate().plusDays(i))
                        .booking(booking)
                        .build()).toList();
        booking.setReservations(reservations);

        bookingRepository.save(booking);
        return null;
    }

    public void cancelReservationById(@Valid Long id) {
        reservationRepository.deleteById(id);
    }
}
