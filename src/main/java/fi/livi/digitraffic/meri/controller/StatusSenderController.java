package fi.livi.digitraffic.meri.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.websocket.LocationsEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselLocationsEndpoint;

@Component
public class StatusSenderController {
    @Scheduled(fixedDelay = 10000)
    public void sendStatus() {
        LocationsEndpoint.sendStatus();
        VesselLocationsEndpoint.sendStatus();
    }
}
