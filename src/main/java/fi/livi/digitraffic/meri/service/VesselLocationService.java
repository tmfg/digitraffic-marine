package fi.livi.digitraffic.meri.service;

import fi.livi.digitraffic.meri.domain.VesselLocation;

public interface VesselLocationService {
    Iterable<VesselLocation> findLocations(final int mmsi, final Long from, final Long to);

    Iterable<VesselLocation> findLocations(final Long from, final Long to);
}
