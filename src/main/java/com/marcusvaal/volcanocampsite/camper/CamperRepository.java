package com.marcusvaal.volcanocampsite.camper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamperRepository extends JpaRepository<Camper, Long> {
    Optional<Camper> findByEmail(String email);
}
