package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselConstruction;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselRegistration;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;
import fi.livi.digitraffic.meri.util.dao.ShortItemRestrictionUtil;

@Service
@ConditionalOnWebApplication
public class VesselDetailsService {
    private final EntityManager entityManager;

    @Autowired
    public VesselDetailsService(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<VesselDetails> findVesselDetails(final ZonedDateTime from, final String vesselName, final Integer mmsi,
            final Integer imo, final List<String> nationalities, final Integer vesselTypeCode) {
        final QueryBuilder qb = new QueryBuilder<>(entityManager, VesselDetails.class, VesselDetails.class);

        if (from != null) {
            qb.gte(qb.get("updateTimestamp"), Date.from(from.toInstant()));
        }
        if (vesselName != null) {
            qb.like("name", '%' + vesselName + '%');
        }
        if (mmsi != null) {
            qb.equals("mmsi", mmsi);
        }
        if (imo != null) {
            qb.equals("imoLloyds", imo);
        }
        if (vesselTypeCode != null) {
            final Join<VesselDetails, VesselConstruction> join = qb.join("vesselConstruction");
            qb.equals(join.get("vesselTypeCode"), vesselTypeCode);
        }
        if (!CollectionUtils.isEmpty(nationalities)) {
            final Join<VesselDetails, VesselRegistration> join = qb.join("vesselRegistration");
            ShortItemRestrictionUtil.addItemRestrictions(qb, join.get("nationality"), nationalities);
        }

        return qb.getResults();
    }
}
