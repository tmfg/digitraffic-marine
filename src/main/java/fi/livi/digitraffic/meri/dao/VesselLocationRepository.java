package fi.livi.digitraffic.meri.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.VesselLocation;

@Repository
public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer> {
    List<VesselLocation> findByMmsiAndTimestampBetween(final int mmsi, final long from, final long to);

    List<VesselLocation> findByTimestampBetween(final long from, final long to);
}
