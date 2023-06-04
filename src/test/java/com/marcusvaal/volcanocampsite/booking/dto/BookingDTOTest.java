package com.marcusvaal.volcanocampsite.booking.dto;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
public class BookingDTOTest {
    @Autowired
    private Validator validator;

    @Test
    @DisplayName("Test validation")
    public void testBookingDtoValidation() {
        BookingDTO bookingDto = new BookingDTO(null, null, null);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDto);
        assertThat(violations, hasSize(3));
    }

    @Test
    @DisplayName("Test field validation")
    public void testBookingDtoFieldValidationTriggers() {
        CamperDTO camper = new CamperDTO(null, "Test Name");
        DateRange dateRange = new DateRange(LocalDate.EPOCH, 3L);
        BookingDTO bookingDto = new BookingDTO(1L, camper, dateRange);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDto);
        assertThat(violations, hasSize(3));
    }
}
