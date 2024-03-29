package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.portnet.PortArea;
import fi.livi.digitraffic.meri.model.portnet.PortAreaKey;
import jakarta.persistence.QueryHint;

@Repository
public interface PortAreaRepository extends JpaRepository<PortArea, PortAreaKey> {
    @QueryHints(@QueryHint(name=AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<PortArea> streamAllBy();

    @QueryHints(@QueryHint(name=AvailableHints.HINT_FETCH_SIZE, value="100"))
    Stream<PortArea> streamByPortAreaKeyLocode(final String locode);
}
