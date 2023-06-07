package com.marcusvaal.volcanocampsite.camper;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import org.springframework.stereotype.Component;

@Component
public class CamperMapper {
    /**
     * Convert Camper to Camper DTO
     * @param camper Camper
     * @return Camper DTO
     */
    public CamperDTO toDto(Camper camper) {
        return new CamperDTO(camper.getEmail(), camper.getFullName());
    }

    /**
     * Conver Camper DTO to Camper
     * @param camperDto Camper DTO
     * @return Camper
     */
    public Camper toCamper(CamperDTO camperDto) {
        return Camper.builder().email(camperDto.email()).fullName(camperDto.fullName()).build();
    }
}
