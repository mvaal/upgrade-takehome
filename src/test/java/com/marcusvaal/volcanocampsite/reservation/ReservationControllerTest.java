package com.marcusvaal.volcanocampsite.reservation;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private MockMvc mockMvc;

    @Test
    void should_return_by_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.email", equalTo("marcus@upgrade.com")))
                .andExpect(jsonPath("$.fullName", equalTo("Marcus")));
    }

    @Test
    void should_return_not_found_if_it_does_not_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/booking/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_by_booking_id_if_it_exists() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/booking/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.email", equalTo("marcus@upgrade.com")))
                .andExpect(jsonPath("$.fullName", equalTo("Marcus")));
    }

    @Test
    void should_return_not_found_if_it_does_not_exists_by_booking_id() throws Exception {
        this.mockMvc.perform(get("/api/v1/reservations/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
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
