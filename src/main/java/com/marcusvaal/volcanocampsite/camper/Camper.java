package com.marcusvaal.volcanocampsite.camper;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.marcusvaal.volcanocampsite.booking.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Camper {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Email
    @NotNull
    private String email;

    @NotNull
    private String fullName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "camper")
    @JsonManagedReference
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Camper{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", bookings=" + bookings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camper camper = (Camper) o;
        return Objects.equals(email, camper.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}