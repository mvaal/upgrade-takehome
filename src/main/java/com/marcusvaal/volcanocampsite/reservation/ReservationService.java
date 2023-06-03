package com.marcusvaal.volcanocampsite.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Optional<Reservation> reservationById(@Valid Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> reservationsByBookingId(Long bookingId) {
        return reservationRepository.findAllByBooking_Id(bookingId);
    }
}
