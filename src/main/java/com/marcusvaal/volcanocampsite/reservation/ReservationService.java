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

    /**
     * Get Reservation by ID
     * @param id Reservation ID
     * @return Reservation
     */
    public Optional<Reservation> reservationById(@Valid Long id) {
        return reservationRepository.findById(id);
    }

    /**
     * Get all Reservations
     * Streams but be Transactional
     * @return Stream of Reservations
     */
    @Transactional
    public Stream<Reservation> allReservations() {
        return reservationRepository.findAll().stream();
    }

    /**
     * Get Reservations by Booking ID
     * Streams but be Transactional
     * @param bookingId Booking ID
     * @return Stream of Reservation
     */
    @Transactional
    public Stream<Reservation> reservationsByBookingId(Long bookingId) {
        return reservationRepository.findAllByBooking_Id(bookingId);
    }

    /**
     * Get available Reservations by date range
     * Streams but be Transactional
     * @param dateRange Date range
     * @return Stream of Reservation
     */
    @Transactional
    public Stream<Reservation> availableReservations(OpenDateRange dateRange) {
        return reservationRepository.findAllByDateBetween(dateRange.startDate(), dateRange.endDate());
    }
}
