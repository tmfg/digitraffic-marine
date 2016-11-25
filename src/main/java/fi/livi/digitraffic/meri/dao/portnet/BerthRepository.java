package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.BerthKey;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthJson;

@Repository
public interface BerthRepository extends JpaRepository<Berth, BerthKey> {
    List<BerthJson> findAllProjectedBy();

    List<BerthJson> findByBerthKeyLocode(final String locode);
}
