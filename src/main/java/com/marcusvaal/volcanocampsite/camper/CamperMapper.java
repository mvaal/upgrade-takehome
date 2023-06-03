package com.marcusvaal.volcanocampsite.camper;

import org.springframework.stereotype.Component;

@Component
public class CamperMapper {
    public CamperDTO toDto(Camper camper) {
        return new CamperDTO(camper.getEmail(), camper.getFullName());
    }

    public Camper toCamper(CamperDTO camperDto) {
        return Camper.builder().email(camperDto.email()).fullName(camperDto.fullName()).build();
    }
}
