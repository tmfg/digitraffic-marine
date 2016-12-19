package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.VesselDetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.VesselDetailsJson;

@Repository
public interface VesselDetailsRepository extends JpaRepository<VesselDetails, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "1000"))
    List<VesselDetailsJson> findByVesselIdInOrderByVesselIdAsc(final List<Long> vesselIds);
}
