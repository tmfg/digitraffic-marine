package fi.livi.digitraffic.meri.service.portnet;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.util.dao.ShortItemRestrictionUtil;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;

@Service
public class PortCallService {
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final PortCallRepository portCallRepository;

    private final EntityManager entityManager;

    private final SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

    public PortCallService(final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallRepository portCallRepository,
                           final EntityManager entityManager) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallRepository = portCallRepository;
        this.entityManager = entityManager;
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
        final QueryBuilder<Long, PortCall> qb = new QueryBuilder<>(entityManager, Long.class, PortCall.class);

        if (date != null) {
            qb.equals(qb.function("to_char", Timestamp.class, qb.<Timestamp>get("portCallTimestamp"), qb.literal("YYYY-MM-dd")),
                formatter.format(date));
        }
        if (from != null) {
            qb.gte(qb.get("portCallTimestamp"), Date.from(from.toInstant()));
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
