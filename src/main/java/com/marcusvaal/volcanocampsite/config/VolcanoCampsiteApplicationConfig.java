package com.marcusvaal.volcanocampsite.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info=@Info(title="Volcano Campsite"))
public class VolcanoCampsiteApplicationConfig {
    /**
     * Enable Observed Aspect
     * @param observationRegistry Observation Registry
     * @return ObservedAspect
     */
    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
