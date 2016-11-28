package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PortCallsJson findPortCalls(final String locode, final Date date) {
        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());
        final List<Long> portCallIds = getPortCallIds(locode, date);

        return new PortCallsJson(
            lastUpdated,
            portCallRepository.findByPortCallIdIn(portCallIds).collect(Collectors.toList())
        );
    }

    private List<Long> getPortCallIds(final String locode, final Date date) {
        final Criteria c = createCriteria().setProjection(Projections.id());

        c.add(Restrictions.sqlRestriction("TO_CHAR(port_call_timestamp, 'yyyy-MM-dd') = TO_CHAR(?, 'yyyy-MM-dd')", date, DateType.INSTANCE));

        if (locode != null) {
            c.add(Restrictions.eq("portToVisit", locode));
        }

        return c.list();
    }
}
