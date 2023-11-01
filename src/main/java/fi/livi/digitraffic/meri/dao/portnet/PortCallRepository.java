package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.dto.portcall.v1.call.PortCallJsonV1;
import fi.livi.digitraffic.meri.model.portnet.PortCall;
import jakarta.persistence.QueryHint;

@Repository
public interface PortCallRepository extends JpaRepository<PortCall, Long> {
    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "10000"))
    @EntityGraph(attributePaths = { "agentInfo", "imoInformation", "portAreaDetails"})
    List<PortCallJsonV1> findByPortCallIdIn(final List<Long> portCallIds);
}
