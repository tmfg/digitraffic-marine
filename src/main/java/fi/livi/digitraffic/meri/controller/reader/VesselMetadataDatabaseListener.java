package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.util.locking.AccessLock;

public class VesselMetadataDatabaseListener implements WebsocketListener {
    private final VesselMetadataRepository vesselMetadataRepository;
    private final AccessLock accessLock;

    public VesselMetadataDatabaseListener(final VesselMetadataRepository vesselMetadataRepository,
                                          final AccessLock accessLock) {
        this.vesselMetadataRepository = vesselMetadataRepository;
        this.accessLock = accessLock;
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if(vm.validate() && accessLock.get()) {
            try {
                vesselMetadataRepository.save(new VesselMetadata(vm.vesselAttributes));
            } finally {
                accessLock.release();
            }
        }
    }
}
