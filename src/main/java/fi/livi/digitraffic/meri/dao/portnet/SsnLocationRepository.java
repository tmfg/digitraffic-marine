package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.portnet.SsnLocation;
import jakarta.persistence.QueryHint;

@Repository
public interface SsnLocationRepository extends JpaRepository<SsnLocation, String> {
    @QueryHints(@QueryHint(name=AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<SsnLocation> streamAllBy();

    SsnLocation findByLocode(final String locode);

    @QueryHints(@QueryHint(name=AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<SsnLocation> streamByCountryIgnoreCase(final String country);
}
