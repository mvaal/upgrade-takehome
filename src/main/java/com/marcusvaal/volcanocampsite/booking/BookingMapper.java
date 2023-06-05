package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.booking.dto.BookingDTO;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
import com.marcusvaal.volcanocampsite.camper.Camper;
import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import com.marcusvaal.volcanocampsite.camper.CamperMapper;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class BookingMapper {
    private final CamperMapper camperMapper;

    public BookingDTO toDto(Booking booking) {
        CamperDTO camper = camperMapper.toDto(booking.getCamper());
        Set<Reservation> reservations = booking.getReservations();
        if (reservations.isEmpty()) {
            throw new RuntimeException("Unexpected empty reservation");
        }
        return new BookingDTO(booking.getId(), camper, new StrictDateRange(Collections.min(reservations).getDate(), Collections.max(reservations).getDate()));
    }

    public Booking toBooking(BookingRequest bookingRequest) {
        Camper camper = camperMapper.toCamper(bookingRequest.camper());
        Booking booking = Booking.builder().camper(camper).build();
        camper.getBookings().add(booking);
        Set<Reservation> reservations = bookingRequest.dateRange().dateStream()
                .map(date -> Reservation.builder().date(date).booking(booking).build())
                .collect(Collectors.toSet());
        booking.setReservations(reservations);
        return booking;
    }
}
