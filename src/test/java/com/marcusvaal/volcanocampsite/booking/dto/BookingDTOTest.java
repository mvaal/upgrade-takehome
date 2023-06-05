package com.marcusvaal.volcanocampsite.booking.dto;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@DisplayName("BookingDTO")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BookingDTOTest {
    @Autowired
    private Validator validator;

    @Test
    public void should_have_violations_for_null_values() {
        BookingDTO bookingDto = new BookingDTO(null, null, null);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDto);
        assertThat(violations, hasSize(3));
    }

    @Test
    public void should_trigger_field_violations() {
        CamperDTO camper = new CamperDTO(null, "Test Name");
        StrictDateRange dateRange = new StrictDateRange(LocalDate.EPOCH, LocalDate.now().plusDays(1));
        BookingDTO bookingDto = new BookingDTO(1L, camper, dateRange);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDto);
        assertThat(violations, hasSize(4));
    }
}
