package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.camper.Camper;
import com.marcusvaal.volcanocampsite.camper.CamperRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CamperRepository camperRepository;

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
    public void cancelBookingById(@Valid Long id) {
        bookingRepository.deleteById(id);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Stream<Booking> allBookings() {
        return bookingRepository.findAll().stream();
    }
}