package fi.livi.digitraffic.meri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.VesselMetadata;

@Repository
public interface VesselMetadataRepository extends JpaRepository<VesselMetadata, Integer> {
}
