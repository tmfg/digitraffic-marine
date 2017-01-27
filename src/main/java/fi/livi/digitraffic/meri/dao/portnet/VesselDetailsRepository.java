package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;

@Repository
public interface VesselDetailsRepository extends JpaRepository<VesselDetails, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "1000"))
    List<VesselDetails> findByVesselIdInOrderByVesselIdAsc(final List<Long> vesselIds);
}
