package com.marcusvaal.volcanocampsite.booking;

import com.marcusvaal.volcanocampsite.camper.Camper;
import com.marcusvaal.volcanocampsite.camper.CamperDTO;
import com.marcusvaal.volcanocampsite.camper.CamperMapper;
import com.marcusvaal.volcanocampsite.camper.CamperRepository;
import com.marcusvaal.volcanocampsite.dto.BookingResponse;
import com.marcusvaal.volcanocampsite.dto.DateRange;
import com.marcusvaal.volcanocampsite.reservation.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
class BookingMapper {
    private final CamperMapper camperMapper;
    private final CamperRepository camperRepository;

    public BookingDTO toDto(Booking booking) {
        CamperDTO camper = camperMapper.toDto(booking.getCamper());
        List<Reservation> reservations = booking.getReservations();
        if (reservations.isEmpty()) {
            throw new RuntimeException("Unexpected empty reservation");
        }
        return new BookingDTO(camper, new DateRange(Collections.min(reservations).getDate(), (long) reservations.size()));
    }

    public Booking toBooking(BookingDTO bookingDto) {
        Camper camper = camperMapper.toCamper(bookingDto.camper());
        Booking booking = Booking.builder().camper(camper).build();
        camper.getBookings().add(booking);
        List<Reservation> reservations = bookingDto.dateRange().dateStream()
                .map(date -> Reservation.builder().date(date).booking(booking).build())
                .toList();
        booking.setReservations(reservations);
        return booking;
    }
}
