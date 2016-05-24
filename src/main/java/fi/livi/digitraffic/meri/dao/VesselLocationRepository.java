package fi.livi.digitraffic.meri.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.livi.digitraffic.meri.domain.VesselLocation;

public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer> {
}
