package com.marcusvaal.volcanocampsite.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.marcusvaal.volcanocampsite.booking.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation implements Comparable<Reservation> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private Booking booking;

    @NotNull
    @Column(name = "stay_date",unique = true)
    private LocalDate date;

    @NotNull
    @Min(value = 0, message = "The campsite should be free for all.")
    @Max(value = 0, message = "The campsite should be free for all.")
    @Builder.Default
    private BigDecimal cost = BigDecimal.valueOf(0);

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", booking=" + booking.getId() +
                ", date=" + date +
                ", cost=" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public int compareTo(Reservation o) {
        return date.compareTo(o.getDate());
    }
}