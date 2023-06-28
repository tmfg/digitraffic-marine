package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import jakarta.persistence.QueryHint;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;

@Repository
public interface PortCallRepository extends JpaRepository<PortCall, Long> {
    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "10000"))
    @EntityGraph(attributePaths = { "agentInfo", "imoInformation", "portAreaDetails"})
    List<PortCallJson> findByPortCallIdIn(final List<Long> portCallIds);
}
