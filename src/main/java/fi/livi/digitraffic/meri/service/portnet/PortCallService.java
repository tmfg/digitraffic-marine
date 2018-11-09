package fi.livi.digitraffic.meri.service.portnet;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.util.TimeUtil;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;
import fi.livi.digitraffic.meri.util.dao.ShortItemRestrictionUtil;

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

    @Transactional(readOnly = true)
    public PortCallsJson findPortCalls(final Date date, final ZonedDateTime from, final ZonedDateTime to, final String locode, final String vesselName,
                                       final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());

        final List<Long> portCallIds = getPortCallIds(date, from, to, locode, vesselName, mmsi, imo, nationality, vesselTypeCode);

        if (CollectionUtils.isEmpty(portCallIds)) {
            return new PortCallsJson(lastUpdated, Collections.emptyList());
        }

        if (portCallIds.size() > 1000) {
            throw new BadRequestException("Too big resultset, try narrow down");
        }

        final List<PortCallJson> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

        return new PortCallsJson(lastUpdated, portCallList);
    }

    private List<Long> getPortCallIds(final Date date, final ZonedDateTime from, ZonedDateTime to, final String locode, final String vesselName,
                                      final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final QueryBuilder<Long, PortCall> qb = new QueryBuilder<>(entityManager, Long.class, PortCall.class);

        if (date != null) {
            qb.gte(qb.<Timestamp>get("portCallTimestamp"), date);
            qb.lt(qb.<Timestamp>get("portCallTimestamp"), DateUtils.addDays(date, 1));
        }
        if (from != null) {
            qb.gte(qb.get("portCallTimestamp"), Date.from(from.toInstant()));
        }
        if (to != null) {
            qb.lt(qb.get("portCallTimestamp"), Date.from(to.toInstant()));
        }
        if (locode != null) {
            qb.equals("portToVisit", locode);
        }
        if (vesselName != null) {
            qb.equals(qb.lower("vesselName"), vesselName.toLowerCase());
        }
        if(mmsi != null) {
            qb.equals("mmsi", mmsi);
        }
        if(imo != null) {
            qb.equals("imoLloyds", imo);
        }

        if(isNotEmpty(nationality)) {
            ShortItemRestrictionUtil.addItemRestrictions(qb, qb.get("nationality"), nationality);
        }

        if(vesselTypeCode != null) {
            qb.equals("vesselTypeCode", vesselTypeCode);
        }

        return qb.getResults( "portCallId");
    }
}
