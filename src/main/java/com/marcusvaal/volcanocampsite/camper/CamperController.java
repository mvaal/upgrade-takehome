package com.marcusvaal.volcanocampsite.camper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/campers")
@RequiredArgsConstructor
public class CamperController {
//    private Logger logger = LoggerFactory.getLogger(CamperController.class);

    private final CamperService camperService;
    private final CamperMapper camperMapper;

    @GetMapping("/{id}")
    public Optional<CamperDTO> camperById(@PathVariable("id") @Valid @NotNull Long id) {
        return camperService.findById(id).map(camperMapper::toDto);
    }

    @GetMapping
    public Stream<CamperDTO> allCampers() {
        return camperService.allCampers().stream().map(camperMapper::toDto);
    }
}
