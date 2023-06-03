package com.marcusvaal.volcanocampsite.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class VolcanoCampsiteApplicationObservationHandler implements ObservationHandler<Observation.Context> {

    private static final Logger log = LoggerFactory.getLogger(VolcanoCampsiteApplicationObservationHandler.class);

    @Override
    public void onStart(Observation.Context context) {
        log.debug("Calling {}", Optional.ofNullable(context.getContextualName()).orElse(context.getName()));
    }

    @Override
    public void onStop(Observation.Context context) {
        log.debug("Exiting {}", Optional.ofNullable(context.getContextualName()).orElse(context.getName()));
    }

    @Override
    public boolean supportsContext(Observation.@NotNull Context context) {
        return true;
    }
}