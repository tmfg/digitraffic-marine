package fi.livi.digitraffic.meri.service;

import java.util.List;

import fi.livi.digitraffic.meri.model.VesselMetadata;

public interface VesselMetadataService {
    VesselMetadata findMetadataByMssi(final int mmsi);

    List<VesselMetadata> listAllVesselMetadata();
}
