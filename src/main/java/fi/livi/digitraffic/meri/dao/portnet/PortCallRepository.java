package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.PortCallJson;

@Repository
public interface PortCallRepository extends JpaRepository<PortCall, Long> {
    List<PortCallJson> findAllProjectedBy();
}
