package com.marcusvaal.volcanocampsite.web.repository;

import com.marcusvaal.volcanocampsite.web.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
