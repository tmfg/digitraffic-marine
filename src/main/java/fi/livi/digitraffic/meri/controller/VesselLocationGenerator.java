package fi.livi.digitraffic.meri.controller;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.model.VesselLocation;

@Component
public class VesselLocationGenerator {
    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedRate = 5000)
    public void tick() {
        final VesselLocation info = new VesselLocation(OffsetDateTime.now(), "12");

        template.convertAndSend("/locations", info);
    }
}
