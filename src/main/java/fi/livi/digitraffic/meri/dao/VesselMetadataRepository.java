package fi.livi.digitraffic.meri.dao;

import fi.livi.digitraffic.meri.domain.VesselMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VesselMetadataRepository extends JpaRepository<VesselMetadata, Integer> {
}
