package com.maan.eway.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ServiceHeartbeat {

    private final AtomicLong heartbeat = new AtomicLong();

    public ServiceHeartbeat(MeterRegistry registry) {

        registry.gauge(
                "springboot_service_heartbeat",
                heartbeat
        );

        heartbeat.set(System.currentTimeMillis());
    }

    @Scheduled(fixedRate = 30000)
    public void updateHeartbeat() {
        heartbeat.set(System.currentTimeMillis());
    }
}
