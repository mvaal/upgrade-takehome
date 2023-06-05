package com.marcusvaal.volcanocampsite.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusvaal.volcanocampsite.booking.dto.BookingRequest;
import com.marcusvaal.volcanocampsite.booking.dto.DateRange;
import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private BookingRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_book_valid_request() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(1);
        CamperDTO camper = new CamperDTO("foo@upgrade.com", "Foo");
        DateRange dateRange = new DateRange(startDate, startDate.plusDays(2));
        BookingRequest bookingRequest = new BookingRequest(camper, dateRange);
        String bookingToCreate = objectMapper.writeValueAsString(bookingRequest);
        this.mockMvc.perform(put("/api/v1/bookings/book")
                        .contentType(APPLICATION_JSON)
                        .content(bookingToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(1)))
                .andExpect(jsonPath("$.camper.email", equalTo("marcus@upgrade.com")))
                .andExpect(jsonPath("$.camper.fullName", equalTo("Marcus")))
                .andExpect(jsonPath("$.dateRange.startDate", equalTo("06/04/2023")))
                .andExpect(jsonPath("$.dateRange.endDate", equalTo("06/06/2023")))
                .andExpect(jsonPath("$.dateRange.duration", equalTo(3)));
    }

    @Test
    void should_return_by_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/bookings/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.bookingId", equalTo(1)))
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
