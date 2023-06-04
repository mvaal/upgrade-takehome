package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.reservation.dto.ReservationDTO;
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
