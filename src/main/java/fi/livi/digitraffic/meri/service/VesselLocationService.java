package fi.livi.digitraffic.meri.service;

import java.util.List;

import fi.livi.digitraffic.meri.domain.VesselLocation;

public interface VesselLocationService {
    List<VesselLocation> findLocations(final int mmsi, final Long from, final Long to);

    List<VesselLocation> findLocations(final Long from, final Long to);
}
