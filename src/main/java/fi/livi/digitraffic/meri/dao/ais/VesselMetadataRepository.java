package fi.livi.digitraffic.meri.dao.ais;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;

@Repository
public interface VesselMetadataRepository extends JpaRepository<VesselMetadata, Integer> {

    VesselMetadataJson findByMmsi(final Integer mmsi);

    @Query(value =
               "SELECT max(v.modified)\n" +
               "FROM vessel v", nativeQuery = true)
    Instant getLastModified();
}
