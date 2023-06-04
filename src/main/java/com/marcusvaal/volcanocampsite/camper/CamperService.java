package com.marcusvaal.volcanocampsite.camper;

import com.marcusvaal.volcanocampsite.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CamperService {
    private final CamperRepository camperRepository;

    public Optional<Camper> findById(Long id){
        return camperRepository.findById(id);
    }

    public Optional<Camper> findByEmail(String email){
        return camperRepository.findByEmail(email);
    }

    public Stream<Camper> allCampers() {
        return camperRepository.findAll().stream();
    }
}
