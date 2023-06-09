package com.marcusvaal.volcanocampsite.camper;

import com.marcusvaal.volcanocampsite.camper.dto.CamperDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/campers")
@Tag(name = "Campers", description = "Camper Info")
@RequiredArgsConstructor
public class CamperController {
    private final Logger logger = LoggerFactory.getLogger(CamperController.class);
    private final CamperService camperService;
    private final CamperMapper camperMapper;

    /**
     * Get camper by ID
     * @param id Camper ID
     * @return Camper DTO
     */
    @GetMapping("/camper/{id}")
    @Operation(summary = "Get Camper By Camper ID", description = "Get Camper by Camper ID - Utility")
    public ResponseEntity<CamperDTO> camperById(@PathVariable("id") @Valid @NotNull Long id) {
        logger.debug("Request - Camper with ID: {}", id);
        return camperService.findById(id)
                .map(camperMapper::toDto)
                .map(camperDto -> new ResponseEntity<>(camperDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get all Campers
     * @return All Campers
     */
    @GetMapping
    @Operation(summary = "Get all campers", description = "Get all Campers - Utility")
    public Stream<CamperDTO> allCampers() {
        logger.debug("Request - All Campers");
        return camperService.allCampers().map(camperMapper::toDto);
    }
}
