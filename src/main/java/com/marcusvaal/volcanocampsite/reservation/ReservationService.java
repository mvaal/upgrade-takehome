package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.booking.dto.OpenDateRange;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Optional<Reservation> reservationById(@Valid Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Stream<Reservation> allReservations() {
        return reservationRepository.findAll().stream();
    }

    @Transactional
    public Stream<Reservation> reservationsByBookingId(Long bookingId) {
        return reservationRepository.findAllByBooking_Id(bookingId);
    }

    @Transactional
    public Stream<Reservation> availableReservations(OpenDateRange dateRange) {
        return reservationRepository.findAllByDateBetween(dateRange.startDate(), dateRange.endDate());
    }
}
