package fi.livi.digitraffic.meri.dao.portnet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortCall;

@Repository
public interface PortCallRepository extends JpaRepository<PortCall, Long> {
}
