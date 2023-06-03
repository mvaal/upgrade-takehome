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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "camper")
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
}