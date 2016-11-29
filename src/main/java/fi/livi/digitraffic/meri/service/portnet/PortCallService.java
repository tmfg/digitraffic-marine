package fi.livi.digitraffic.meri.service.portnet;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;

@Service
public class PortCallService {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final EntityManager entityManager;

    public PortCallService(final PortCallRepository portCallRepository,
                           final UpdatedTimestampRepository updatedTimestampRepository,
                           final EntityManager entityManager) {
        this.portCallRepository = portCallRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.entityManager = entityManager;
    }

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class)
                .createCriteria(PortCall.class)
                .setFetchSize(1000);
    }

    @Transactional(readOnly = true)
    public PortCallsJson findPortCalls(final Date date, final ZonedDateTime from, final String locode, final String vesselName) {
        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());

        final List<Long> portCallIds = getPortCallIds(date, from, locode, vesselName);

        final Criteria c = createCriteria();

        Disjunction orConditions = Restrictions.disjunction();
        for (List<Long> ids : Lists.partition(portCallIds, 1000)) {
            orConditions.add(Restrictions.in("portCallId", ids));
        }
        c.add(Restrictions.and(orConditions));

        return new PortCallsJson(lastUpdated, c.list());
    }

    private List<Long> getPortCallIds(final Date date, final ZonedDateTime from, final String locode, final String vesselName) {
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

        return c.list();
    }
}
