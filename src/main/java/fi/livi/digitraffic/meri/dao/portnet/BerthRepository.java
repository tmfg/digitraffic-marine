package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.BerthKey;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthJson;

@Repository
public interface BerthRepository extends JpaRepository<Berth, BerthKey> {
    Stream<BerthJson> findAllProjectedBy();

    Stream<BerthJson> findByBerthKeyLocode(final String locode);
}
