package com.marcusvaal.volcanocampsite.camper;

import com.marcusvaal.volcanocampsite.reservation.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamperService {
    private CamperRepository camperRepository;

    public Optional<Camper> findById(Long id){
        return camperRepository.findById(id);
    }

    public Optional<Camper> findByEmail(String email){
        return camperRepository.findByEmail(email);
    }

    public List<Camper> allCampers() {
        return camperRepository.findAll();
    }
}
