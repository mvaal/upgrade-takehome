package com.marcusvaal.volcanocampsite.reservation;

import com.marcusvaal.volcanocampsite.reservation.dto.ReservationDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    /**
     * Convert Reservation to Reservation DTO
     * @param reservation Reservation
     * @return Reservation DTO
     */
    public ReservationDTO toDto(Reservation reservation) {
        return new ReservationDTO(reservation.getDate(), reservation.getCost());
    }

    /**
     * Convert Reservation DTO to Reservation
     * @param reservationDto Reservation DTO
     * @return Reservation
     */
    public Reservation toReservation(ReservationDTO reservationDto) {
        return Reservation.builder().date(reservationDto.date()).cost(reservationDto.cost()).build();
    }
}
