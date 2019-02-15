package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaKey;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Repository
public interface PortAreaRepository extends JpaRepository<PortArea, PortAreaKey> {
    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="0"))
    Stream<PortArea> streamAllBy();

    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="0"))
    Stream<PortArea> streamByPortAreaKeyLocode(final String locode);
}
