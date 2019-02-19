package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Repository
public interface SsnLocationRepository extends JpaRepository<SsnLocation, String> {
    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="100"))
    Stream<SsnLocation> streamAllBy();

    SsnLocation findByLocode(final String locode);

    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="100"))
    Stream<SsnLocation> streamByCountryIgnoreCase(final String country);
}
