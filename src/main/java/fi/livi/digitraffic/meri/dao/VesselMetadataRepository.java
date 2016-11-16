package fi.livi.digitraffic.meri.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;

@Repository
public interface VesselMetadataRepository extends JpaRepository<VesselMetadata, Integer> {
    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="1000"))
    List<VesselMetadataJson> findAllProjectedBy();

    VesselMetadataJson findByMmsi(final Integer mmsi);
}
