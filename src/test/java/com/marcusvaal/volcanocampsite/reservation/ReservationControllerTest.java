package com.marcusvaal.volcanocampsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusvaal.volcanocampsite.booking.dto.OpenDateRange;
import com.marcusvaal.volcanocampsite.booking.dto.StrictDateRange;
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
@DisplayName("ReservationController")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/camper-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/booking-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/reservation-data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class ReservationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_by_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.date", equalTo("2023-06-01")))
                .andExpect(jsonPath("$.cost", equalTo(0d)));
    }

    @Test
    void should_return_not_found_if_it_does_not_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_by_booking_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/booking/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void should_return_not_found_if_it_does_not_exists_by_booking_id() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/booking/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_available_reservations_no_end_date() throws Exception {
        LocalDate startDate = LocalDate.parse("2023-06-01");
        OpenDateRange dateRange = new OpenDateRange(startDate, null);
        String dateRangeToQuery = objectMapper.writeValueAsString(dateRange);
        this.mockMvc.perform(post("/api/v1/reservations/available")
                        .contentType(APPLICATION_JSON)
                        .content(dateRangeToQuery))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void should_return_available_reservations() throws Exception {
        LocalDate startDate = LocalDate.parse("2023-06-01");
        LocalDate endDate = LocalDate.parse("2023-06-01");
        OpenDateRange dateRange = new OpenDateRange(startDate, endDate);
        String dateRangeToQuery = objectMapper.writeValueAsString(dateRange);
        this.mockMvc.perform(post("/api/v1/reservations/available")
                        .contentType(APPLICATION_JSON)
                        .content(dateRangeToQuery))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void should_return_all_reservations() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
