package fi.livi.digitraffic.meri.controller.reader;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.util.service.LockingService;

public class VesselLocationDatabaseListener implements WebsocketListener {
    private final VesselLocationRepository vesselLocationRepository;
    private final LockingService lockingService;

    private final String instanceId;

    public VesselLocationDatabaseListener(final VesselLocationRepository vesselLocationRepository,
                                          final LockingService lockingService) {
        this.vesselLocationRepository = vesselLocationRepository;
        this.lockingService = lockingService;
        this.instanceId = UUID.randomUUID().toString();
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if(ais.validate() && getLock()) {
            vesselLocationRepository.save(new VesselLocation(ais));
        }
    }

    private boolean getLock() {
        return lockingService.acquireLock("AIS", instanceId, 2);
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
