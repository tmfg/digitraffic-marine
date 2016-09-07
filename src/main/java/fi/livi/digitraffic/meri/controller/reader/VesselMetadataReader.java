package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.VesselMetadata;
import fi.livi.digitraffic.meri.model.VesselMessage;
import fi.livi.util.locking.AccessLock;

public class VesselMetadataReader extends WebsocketReader<VesselMessage> {
    private final VesselMetadataRepository vesselMetadataRepository;
    private final AccessLock accessLock;

    public VesselMetadataReader(final String vesselMetadataUrl,
                                final VesselMetadataRepository vesselMetadataRepository,
                                final AccessLock accessLock) {
        super(vesselMetadataUrl);
        this.vesselMetadataRepository = vesselMetadataRepository;
        this.accessLock = accessLock;
    }

    @Override
    protected VesselMessage convert(final String message) {
        return MessageConverter.convertMetadata(message);
    }

    @Override
    protected void handleMessage(final VesselMessage message) {
        if(accessLock.get()) {
            try {
                vesselMetadataRepository.save(new VesselMetadata(message.vesselAttributes));
            } finally {
                accessLock.release();
            }
        }
    }
}
