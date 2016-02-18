package fi.livi.digitraffic.meri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.VesselLocation;

@Repository
public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer> {
}
