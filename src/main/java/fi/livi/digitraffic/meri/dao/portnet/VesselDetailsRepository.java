package fi.livi.digitraffic.meri.dao.portnet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.VesselDetails.VesselDetails;

@Repository
public interface VesselDetailsRepository extends JpaRepository<VesselDetails, Long> {
}
