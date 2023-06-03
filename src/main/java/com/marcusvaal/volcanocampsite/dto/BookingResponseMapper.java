package com.marcusvaal.volcanocampsite.dto;

import com.marcusvaal.volcanocampsite.booking.Booking;
import com.marcusvaal.volcanocampsite.camper.CamperDTO;
import com.marcusvaal.volcanocampsite.camper.CamperMapper;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingResponseMapper {
    private CamperMapper camperMapper;

    public BookingResponse toDto(Booking booking) {
        CamperDTO camper = camperMapper.toDto(booking.getCamper());
        List<Reservation> reservations = booking.getReservations();
        if (reservations.isEmpty()) {
            throw new RuntimeException("Unexpected empty reservation");
        }
        return new BookingResponse(camper, new DateRange(Collections.min(reservations).getDate(), (long) reservations.size()));
    }
}
