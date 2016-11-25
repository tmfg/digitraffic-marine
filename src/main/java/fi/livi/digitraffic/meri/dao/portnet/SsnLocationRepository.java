package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationJson;

@Repository
public interface SsnLocationRepository extends JpaRepository<SsnLocation, String> {
    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="1000"))
    List<SsnLocationJson> findAllLocationsProjectedBy();

    SsnLocationJson findByLocode(final String locode);

    List<SsnLocationJson> findByCountryIgnoreCase(final String country);
}
