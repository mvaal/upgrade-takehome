package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
import com.marcusvaal.volcanocampsite.camper.Camper;
import com.marcusvaal.volcanocampsite.camper.CamperRepository;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import com.marcusvaal.volcanocampsite.reservation.ReservationRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final CamperRepository camperRepository;
    private final BookingRepository bookingRepository;
    private final ReservationRepository reservationRepository;


    @Observed(name = "booking.book.duration.days", contextualName = "BookingService.bookDuration")
    @Transactional
    public Booking bookDuration(Booking booking) {
        // Ideally this wouldn't create as part of the request, but instead
        // when the user registers, and we would fail out if findByEmail is empty.
        // Not part of assignment criteria.
        Camper camper = camperRepository
                .findByEmail(booking.getCamper().getEmail())
                .orElse(booking.getCamper());
        camper.getBookings().add(booking);
        booking.setCamper(camper);
        return bookingRepository.save(booking);
    }

    @Observed(name = "booking.cancel.booking", contextualName = "BookingService.cancelBookingById")
    public void deleteById(@Valid Long id) {
        bookingRepository.deleteById(id);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }

    public Stream<Booking> allBookings() {
        return bookingRepository.findAll().stream();
    }

    @Transactional
    public Optional<Booking> updateDuration(Long id, final StrictDateRange dateRange) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    List<Reservation> reservations = dateRange.dateStream()
                            .map(date -> Reservation.builder().date(date).booking(booking).build())
                            .toList();
                    booking.getReservations().retainAll(reservations);
                    Set<Reservation> newReservations = reservations.stream()
                            .filter(reservation -> !booking.getReservations()
                                    .contains(reservation))
                            .collect(Collectors.toSet());
                    booking.getReservations().addAll(newReservations);
                    return booking;
                })
                .map(bookingRepository::save);
    }
}
