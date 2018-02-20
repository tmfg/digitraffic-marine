package fi.livi.digitraffic.meri.service.portnet;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.not;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
                                       final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());

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
                                      final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final Criteria c = createCriteria().setProjection(Projections.id());

        if (date != null) {
            c.add(Restrictions.sqlRestriction("TO_CHAR(port_call_timestamp, 'yyyy-MM-dd') = TO_CHAR(?, 'yyyy-MM-dd')", date, DateType.INSTANCE));
        }
        if (from != null) {
            c.add(Restrictions.gt("portCallTimestamp", new Timestamp(from.toEpochSecond() * 1000)));
        }
        if (locode != null) {
            c.add(eq("portToVisit", locode));
        }
        if (vesselName != null) {
            c.add(Restrictions.sqlRestriction("lower(vessel_name) = lower(?)", vesselName, StringType.INSTANCE));
        }
        if(mmsi != null) {
            c.add(eq("mmsi", mmsi));
        }
        if(imo != null) {
            c.add(eq("imoLloyds", imo));
        }

        if(isNotEmpty(nationality)) {
            addNationalityRestriction(c, nationality);
        }

        if(vesselTypeCode != null) {
            c.add(eq("vesselTypeCode", vesselTypeCode));
        }

        return c.list();
    }

    private void addNationalityRestriction(final Criteria c, final List<String> nationality) {
        final List<String> notInList = nationality.stream().filter(n -> StringUtils.startsWith(n, "!")).map(z -> z.substring(1)).collect(Collectors.toList());
        final List<String> inList = nationality.stream().filter(n -> !StringUtils.startsWith(n, "!")).collect(Collectors.toList());

        if(isNotEmpty(inList)) {
            c.add(in("nationality", inList));
    }

        if(isNotEmpty(notInList)) {
            c.add(not(in("nationality", notInList)));
        }
    }
}
