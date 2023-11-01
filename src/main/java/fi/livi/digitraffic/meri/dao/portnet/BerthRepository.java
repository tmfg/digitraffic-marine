package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.portnet.Berth;
import fi.livi.digitraffic.meri.model.portnet.BerthKey;
import jakarta.persistence.QueryHint;

@Repository
public interface BerthRepository extends JpaRepository<Berth, BerthKey> {
    @QueryHints(@QueryHint(name= AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<Berth> streamAllBy();

    @QueryHints(@QueryHint(name=AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<Berth> streamByBerthKeyLocode(final String locode);
}
