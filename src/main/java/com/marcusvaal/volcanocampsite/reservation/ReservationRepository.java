package com.marcusvaal.volcanocampsite.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Stream<Reservation> findAllByBooking_Id(Long bookingId);
}
