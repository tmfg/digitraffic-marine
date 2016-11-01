package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationJson;

@Repository
public interface SsnLocationRepository extends JpaRepository<SsnLocation, String> {
    Stream<SsnLocationJson> findAllLocationsProjectedBy();

    SsnLocationJson findByLocode(final String locode);
}
