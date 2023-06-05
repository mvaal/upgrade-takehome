package com.marcusvaal.volcanocampsite.booking.dto;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
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
@DisplayName("BookingRequest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BookingRequestTest {
    @Autowired
    private Validator validator;

    @Test
    public void should_have_violations_for_null_values() {
        BookingRequest bookingRequest = new BookingRequest(null, null);
        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingRequest);
        assertThat(violations, hasSize(2));
    }

    @Test
    public void should_trigger_field_violations() {
        CamperDTO camper = new CamperDTO(null, "Test Name");
        DateRange dateRange = new DateRange(LocalDate.EPOCH, 3L);
        BookingRequest bookingDto = new BookingRequest(camper, dateRange);
        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingDto);
        assertThat(violations, hasSize(3));
    }
}
