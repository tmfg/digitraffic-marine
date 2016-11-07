package fi.livi.digitraffic.meri.service.portnet;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;

@Service
public class PortcallService {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final EntityManager entityManager;

    public PortcallService(final PortCallRepository portCallRepository,
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
    public PortCallsJson listAllPortCalls(final String locode, final ZonedDateTime from) {
        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());
        final List<Long> portCallIds = getPortCallIds(locode, from);

        return new PortCallsJson(
            lastUpdated,
            portCallRepository.findByPortCallIdIn(portCallIds).collect(Collectors.toList())
        );
    }

    private List<Long> getPortCallIds(final String locode, final ZonedDateTime from) {
        final Criteria c = createCriteria().setProjection(Projections.id());

        if(locode != null) {
            c.add(Restrictions.eq("portToVisit", locode));
        }

        if(from != null) {
            c.add(Restrictions.gt("portCallTimestamp", new Timestamp(from.toEpochSecond() * 1000)));
        }

        return c.list();
    }
}
