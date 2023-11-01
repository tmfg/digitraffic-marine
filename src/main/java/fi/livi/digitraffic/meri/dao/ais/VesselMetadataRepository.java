package fi.livi.digitraffic.meri.dao.ais;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.dto.ais.v1.VesselMetadataJsonV1;
import fi.livi.digitraffic.meri.model.ais.VesselMetadata;

@Repository
public interface VesselMetadataRepository extends JpaRepository<VesselMetadata, Integer> {

    VesselMetadataJsonV1 findByMmsi(final Integer mmsi);

    @Query(value =
               "SELECT max(v.modified)\n" +
               "FROM vessel v", nativeQuery = true)
    Instant getLastModified();
}
