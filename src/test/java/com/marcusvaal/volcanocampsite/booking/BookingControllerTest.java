package com.marcusvaal.volcanocampsite.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
import com.marcusvaal.volcanocampsite.camper.CamperRepository;
import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import com.marcusvaal.volcanocampsite.reservation.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("BookingController")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/camper-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/booking-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/reservation-data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class BookingControllerTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CamperRepository camperRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_book_valid_request_with_new_camper() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.now().plusDays(1);
        long duration = 3;
        CamperDTO camper = new CamperDTO("foo@upgrade.com", "Foo");
        StrictDateRange dateRange = new StrictDateRange(startDate, startDate.plusDays(duration-1));
        BookingRequest bookingRequest = new BookingRequest(camper, dateRange);
        String bookingToCreate = objectMapper.writeValueAsString(bookingRequest);

        assertThat(this.camperRepository.findAll(), hasSize(1));
        assertThat(this.bookingRepository.findAll(), hasSize(1));
        assertThat(this.reservationRepository.findAll(), hasSize(3));

        this.mockMvc.perform(put("/api/v1/bookings/book")
                        .contentType(APPLICATION_JSON)
                        .content(bookingToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(1)))
                .andExpect(jsonPath("$.camper.email", equalTo(camper.email())))
                .andExpect(jsonPath("$.camper.fullName", equalTo(camper.fullName())))
                .andExpect(jsonPath("$.dateRange.startDate", equalTo(formatter.format(startDate))))
                .andExpect(jsonPath("$.dateRange.endDate", equalTo(formatter.format(dateRange.endDate()))))
                .andExpect(jsonPath("$.dateRange.durationDays", equalTo((int)duration)));

        assertThat(this.camperRepository.findAll(), hasSize(2));
        assertThat(this.bookingRepository.findAll(), hasSize(2));
        assertThat(this.reservationRepository.findAll(), hasSize(6));
    }

    @Test
    void should_book_valid_request_with_existing_camper() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.now().plusDays(1);
        long duration = 3;
        CamperDTO camper = new CamperDTO("marcus@upgrade.com", "Marcus");
        StrictDateRange dateRange = new StrictDateRange(startDate, startDate.plusDays(duration-1));
        BookingRequest bookingRequest = new BookingRequest(camper, dateRange);
        String bookingToCreate = objectMapper.writeValueAsString(bookingRequest);

        assertThat(this.camperRepository.findAll(), hasSize(1));
        assertThat(this.bookingRepository.findAll(), hasSize(1));
        assertThat(this.reservationRepository.findAll(), hasSize(3));

        this.mockMvc.perform(put("/api/v1/bookings/book")
                        .contentType(APPLICATION_JSON)
                        .content(bookingToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(2)))
                .andExpect(jsonPath("$.camper.email", equalTo(camper.email())))
                .andExpect(jsonPath("$.camper.fullName", equalTo(camper.fullName())))
                .andExpect(jsonPath("$.dateRange.startDate", equalTo(formatter.format(startDate))))
                .andExpect(jsonPath("$.dateRange.endDate", equalTo(formatter.format(dateRange.endDate()))))
                .andExpect(jsonPath("$.dateRange.durationDays", equalTo((int)duration)));

        assertThat(this.camperRepository.findAll(), hasSize(1));
        assertThat(this.bookingRepository.findAll(), hasSize(2));
        assertThat(this.reservationRepository.findAll(), hasSize(6));
    }

    @Test
    void should_not_book_bookings_on_same_day() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.now().plusDays(1);
        long durationOne = 3;
        CamperDTO camperOne = new CamperDTO("foo@upgrade.com", "Foo");
        StrictDateRange dateRangeOne = new StrictDateRange(startDate, startDate.plusDays(durationOne-1));
        BookingRequest bookingRequestOne = new BookingRequest(camperOne, dateRangeOne);
        String bookingToCreateOne = objectMapper.writeValueAsString(bookingRequestOne);

        CamperDTO camperTwo = new CamperDTO("bar@upgrade.com", "Bar");
        StrictDateRange dateRangeTwo = new StrictDateRange(startDate, startDate);
        BookingRequest bookingRequestTwo = new BookingRequest(camperTwo, dateRangeTwo);
        String bookingToCreateTwo = objectMapper.writeValueAsString(bookingRequestTwo);

        assertThat(this.camperRepository.findAll(), hasSize(1));
        assertThat(this.bookingRepository.findAll(), hasSize(1));
        assertThat(this.reservationRepository.findAll(), hasSize(3));

        this.mockMvc.perform(put("/api/v1/bookings/book")
                        .contentType(APPLICATION_JSON)
                        .content(bookingToCreateOne))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(3)))
                .andExpect(jsonPath("$.camper.email", equalTo(camperOne.email())))
                .andExpect(jsonPath("$.camper.fullName", equalTo(camperOne.fullName())))
                .andExpect(jsonPath("$.dateRange.startDate", equalTo(formatter.format(startDate))))
                .andExpect(jsonPath("$.dateRange.endDate", equalTo(formatter.format(dateRangeOne.endDate()))))
                .andExpect(jsonPath("$.dateRange.durationDays", equalTo((int)durationOne)));

        assertThat(this.camperRepository.findAll(), hasSize(2));
        assertThat(this.bookingRepository.findAll(), hasSize(2));
        assertThat(this.reservationRepository.findAll(), hasSize(6));

        this.mockMvc.perform(put("/api/v1/bookings/book")
                        .contentType(APPLICATION_JSON)
                        .content(bookingToCreateTwo))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo("Booking is attempting to be scheduled on an existing reserved date")));

        assertThat(this.camperRepository.findAll(), hasSize(2));
        assertThat(this.bookingRepository.findAll(), hasSize(2));
        assertThat(this.reservationRepository.findAll(), hasSize(6));
    }

    @Test
    void should_return_by_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/bookings/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(-1)))
                .andExpect(jsonPath("$.camper.email", equalTo("marcus@upgrade.com")))
                .andExpect(jsonPath("$.camper.fullName", equalTo("Marcus")))
                .andExpect(jsonPath("$.dateRange.startDate", equalTo("06/01/2023")))
                .andExpect(jsonPath("$.dateRange.endDate", equalTo("06/03/2023")))
                .andExpect(jsonPath("$.dateRange.durationDays", equalTo(3)));
    }

    @Test
    void should_return_not_found_if_it_does_not_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/bookings/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_bookings() throws Exception {
        this.mockMvc.perform(get("/api/v1/bookings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
