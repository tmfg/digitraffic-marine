package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.model.AISMessage;
import fi.livi.util.locking.AccessLock;

public class VesselLocationDatabaseListener implements WebsocketListener {
    private final VesselLocationRepository vesselLocationRepository;
    private final AccessLock accessLock;

    public VesselLocationDatabaseListener(final VesselLocationRepository vesselLocationRepository,
                                          final AccessLock accessLock) {
        this.vesselLocationRepository = vesselLocationRepository;
        this.accessLock = accessLock;
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if(ais.validate() && accessLock.get()) {
            try {
                vesselLocationRepository.save(new VesselLocation(ais));
            } finally {
                accessLock.release();
            }
        }
    }
}
