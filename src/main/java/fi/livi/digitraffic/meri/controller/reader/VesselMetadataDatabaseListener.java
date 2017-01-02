package fi.livi.digitraffic.meri.controller.reader;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.util.service.LockingService;

public class VesselMetadataDatabaseListener implements WebsocketListener {
    private final VesselMetadataRepository vesselMetadataRepository;
    private final LockingService lockingService;

    private final String instanceId;

    public VesselMetadataDatabaseListener(final VesselMetadataRepository vesselMetadataRepository,
                                          final LockingService lockingService) {
        this.vesselMetadataRepository = vesselMetadataRepository;
        this.lockingService = lockingService;

        this.instanceId = UUID.randomUUID().toString();
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if(vm.validate() && getLock()) {
            vesselMetadataRepository.save(new VesselMetadata(vm.vesselAttributes));
        }
    }

    private boolean getLock() {
        return lockingService.acquireLock("AIS", instanceId, 2);
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
