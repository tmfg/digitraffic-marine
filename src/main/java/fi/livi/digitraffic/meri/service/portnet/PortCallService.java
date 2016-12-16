package fi.livi.digitraffic.meri.service.portnet;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;

@Service
public class PortCallService {
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final PortCallRepository portCallRepository;

    private final EntityManager entityManager;

    public PortCallService(final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallRepository portCallRepository,
                           final EntityManager entityManager) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallRepository = portCallRepository;
        this.entityManager = entityManager;
    }

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class)
                .createCriteria(PortCall.class)
                .setFetchSize(1000);
    }

    @Transactional(readOnly = true)
    public PortCallsJson findPortCalls(final Date date, final ZonedDateTime from, final String locode, final String vesselName,
                                       final Integer mmsi, final Integer imo, final String nationality, final Integer vesselTypeCode) {
        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());

        final List<Long> portCallIds = getPortCallIds(date, from, locode, vesselName, mmsi, imo, nationality, vesselTypeCode);

        if (CollectionUtils.isEmpty(portCallIds)) {
            return new PortCallsJson(lastUpdated, Collections.emptyList());
        }

        if (portCallIds.size() > 1000) {
            throw new BadRequestException("Too big resultset, try narrow down");
        }

        final List<PortCallJson> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

        return new PortCallsJson(lastUpdated, portCallList);
    }

    private List<Long> getPortCallIds(final Date date, final ZonedDateTime from, final String locode, final String vesselName,
                                      final Integer mmsi, final Integer imo, final String nationality, final Integer vesselTypeCode) {
        final Criteria c = createCriteria().setProjection(Projections.id());

        if (date != null) {
            c.add(Restrictions.sqlRestriction("TO_CHAR(port_call_timestamp, 'yyyy-MM-dd') = TO_CHAR(?, 'yyyy-MM-dd')", date, DateType.INSTANCE));
        }
        if (from != null) {
            c.add(Restrictions.gt("portCallTimestamp", new Timestamp(from.toEpochSecond() * 1000)));
        }
        if (locode != null) {
            c.add(Restrictions.eq("portToVisit", locode));
        }
        if (vesselName != null) {
            c.add(Restrictions.sqlRestriction("lower(vessel_name) = lower(?)", vesselName, StringType.INSTANCE));
        }
        if(mmsi != null) {
            c.add(Restrictions.eq("mmsi", mmsi));
        }
        if(imo != null) {
            c.add(Restrictions.eq("imoLloyds", imo));
        }
        if(nationality != null) {
            c.add(Restrictions.eq("nationality", nationality));
        }
        if(vesselTypeCode != null) {
            c.add(Restrictions.eq("vesselTypeCode", vesselTypeCode));
        }

        return c.list();
    }
}
