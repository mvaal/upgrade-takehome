package com.marcusvaal.volcanocampsite.reservation;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationDTO toDto(Reservation reservation) {
        return new ReservationDTO(reservation.getDate(), reservation.getCost());
    }

    public Reservation toCamper(ReservationDTO reservationDto) {
        return Reservation.builder().date(reservationDto.date()).cost(reservationDto.cost()).build();
    }
}
