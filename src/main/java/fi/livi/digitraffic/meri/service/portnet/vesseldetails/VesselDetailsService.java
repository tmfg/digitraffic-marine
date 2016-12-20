package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.not;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import fi.livi.digitraffic.meri.domain.portnet.VesselDetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.VesselDetailsJson;

@Service
public class VesselDetailsService {

    private final VesselDetailsRepository vesselDetailsRepository;
    private final EntityManager entityManager;

    @Autowired
    public VesselDetailsService(final VesselDetailsRepository vesselDetailsRepository,
                                final EntityManager entityManager) {
        this.vesselDetailsRepository = vesselDetailsRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<VesselDetailsJson> findVesselDetails(final ZonedDateTime from, final String vesselName, final Integer mmsi, final Integer imo,
                                                     final List<String> nationalities, final Integer vesselTypeCode) {

        final List<Long> vesselIds = getVesselDetailsIds(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);

        return vesselDetailsRepository.findByVesselIdInOrderByVesselIdAsc(vesselIds);
    }

    private List<Long> getVesselDetailsIds(ZonedDateTime from, String vesselName, Integer mmsi, Integer imo, List<String> nationalities,
                                           Integer vesselTypeCode) {
        Criteria c = createCriteria().setProjection(Projections.id())
                                     .createAlias("vesselConstruction", "vesselConstruction")
                                     .createAlias("vesselRegistration", "vesselRegistration");

        if (from != null) {
            c.add(Restrictions.gt("updateTimestamp", new Timestamp(from.toEpochSecond() * 1000)));
        }
        if (vesselName != null) {
            c.add(Restrictions.sqlRestriction("lower(name) = lower(?)", vesselName, StringType.INSTANCE));
        }
        if (mmsi != null) {
            c.add(eq("mmsi", mmsi));
        }
        if (imo != null) {
            c.add(eq("imoLloyds", imo));
        }
        if (vesselTypeCode != null) {
            c.add(eq("vesselConstruction.vesselTypeCode", vesselTypeCode));
        }
        if (!CollectionUtils.isEmpty(nationalities)) {
            addInRestriction(c, "vesselRegistration.nationality", nationalities);
        }
        return c.list();
    }

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class)
                .createCriteria(VesselDetails.class)
                .setFetchSize(1000);
    }

    // TODO: Replace PortCallService.addNationalityRestriction with this and create an util class or something
    private static void addInRestriction(final Criteria c, final String property, final List<String> strings) {
        final List<String> notInList = strings.stream().filter(n -> StringUtils.startsWith(n, "!")).map(z -> z.substring(1)).collect(Collectors.toList());
        final List<String> inList = strings.stream().filter(n -> !StringUtils.startsWith(n, "!")).collect(Collectors.toList());

        if(isNotEmpty(inList)) {
            c.add(in(property, inList));
        }

        if(isNotEmpty(notInList)) {
            c.add(not(in(property, notInList)));
        }
    }
}
