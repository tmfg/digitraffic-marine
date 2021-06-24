package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_CALLS;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Path;

import fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails;
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
    public PortCallsJson findPortCalls(
        final Date modifiedDate,
        final ZonedDateTime modifiedFrom,
        final ZonedDateTime modifiedTo,
        final ZonedDateTime etaFrom,
        final ZonedDateTime etaTo,
        final ZonedDateTime etdFrom,
        final ZonedDateTime etdTo,
        final ZonedDateTime ataFrom,
        final ZonedDateTime ataTo,
        final ZonedDateTime atdFrom,
        final ZonedDateTime atdTo,
        final String locode,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        return doFindPortCalls(modifiedDate,
            modifiedFrom,
            modifiedTo,
            etaFrom,
            etaTo,
            etdFrom,
            etdTo,
            ataFrom,
            ataTo,
            atdFrom,
            atdTo,
            locode,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }

    @Transactional(readOnly = true)
    public PortCallsJson findPortCallsWithTimestamps(
        final Date modifiedDate,
        final ZonedDateTime modifiedFrom,
        final ZonedDateTime etaFrom,
        final ZonedDateTime etaTo,
        final ZonedDateTime etdFrom,
        final ZonedDateTime etdTo,
        final ZonedDateTime ataFrom,
        final ZonedDateTime ataTo,
        final ZonedDateTime atdFrom,
        final ZonedDateTime atdTo,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        return doFindPortCalls(modifiedDate,
            modifiedFrom,
            null,
            etaFrom,
            etaTo,
            etdFrom,
            etdTo,
            ataFrom,
            ataTo,
            atdFrom,
            atdTo,
            null,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }

    @Transactional(readOnly = true)
    public PortCallsJson findPortCallsWithoutTimestamps(
        final ZonedDateTime modifiedFrom,
        final ZonedDateTime modifiedTo,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        return doFindPortCalls(null,
            modifiedFrom,
            modifiedTo,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }

    private PortCallsJson doFindPortCalls(
        final Date modifiedDate,
        final ZonedDateTime modifiedFrom,
        final ZonedDateTime modifiedTo,
        final ZonedDateTime etaFrom,
        final ZonedDateTime etaTo,
        final ZonedDateTime etdFrom,
        final ZonedDateTime etdTo,
        final ZonedDateTime ataFrom,
        final ZonedDateTime ataTo,
        final ZonedDateTime atdFrom,
        final ZonedDateTime atdTo,
        final String locode,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(PORT_CALLS);

        final List<Long> portCallIds = getPortCallIds(modifiedDate,
            modifiedFrom,
            modifiedTo,
            etaFrom,
            etaTo,
            etdFrom,
            etdTo,
            ataFrom,
            ataTo,
            atdFrom,
            atdTo,
            locode,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);

        if (CollectionUtils.isEmpty(portCallIds)) {
            return new PortCallsJson(lastUpdated, Collections.emptyList());
        }

        if (portCallIds.size() > 1000) {
            throw new BadRequestException("The search result is too big (over 1000 items), try to narrow down your search criteria.");
        }

        final List<PortCallJson> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

        return new PortCallsJson(lastUpdated, portCallList);
    }

    private List<Long> getPortCallIds(
        final Date modifiedDate,
        final ZonedDateTime modifiedFrom,
        final ZonedDateTime modifiedTo,
        final ZonedDateTime etaFrom,
        final ZonedDateTime etaTo,
        final ZonedDateTime etdFrom,
        final ZonedDateTime etdTo,
        final ZonedDateTime ataFrom,
        final ZonedDateTime ataTo,
        final ZonedDateTime atdFrom,
        final ZonedDateTime atdTo,
        final String locode,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        final QueryBuilder<Long, PortCall> qb = new QueryBuilder<>(entityManager, Long.class, PortCall.class);

        if (modifiedDate != null) {
            qb.gte(qb.<Timestamp>get("portCallTimestamp"), modifiedDate);
            qb.lt(qb.<Timestamp>get("portCallTimestamp"), DateUtils.addDays(modifiedDate, 1));
        }
        if (modifiedFrom != null) {
            qb.gte(qb.get("portCallTimestamp"), Date.from(modifiedFrom.toInstant()));
        }
        if (modifiedTo != null) {
            qb.lt(qb.get("portCallTimestamp"), Date.from(modifiedTo.toInstant()));
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

        final Path<PortAreaDetails> portAreaDetails = qb.join("portAreaDetails");

        if (etaFrom != null) {
            qb.gte(portAreaDetails.get("eta"), Date.from(etaFrom.toInstant()));
        }

        if (etaTo != null) {
            qb.lt(portAreaDetails.get("eta"), Date.from(etaTo.toInstant()));
        }

        if (etdFrom != null) {
            qb.gte(portAreaDetails.get("etd"), Date.from(etdFrom.toInstant()));
        }

        if (etdTo != null) {
            qb.lt(portAreaDetails.get("etd"), Date.from(etdTo.toInstant()));
        }

        if (ataFrom != null) {
            qb.gte(portAreaDetails.get("ata"), Date.from(ataFrom.toInstant()));
        }

        if (ataTo != null) {
            qb.lt(portAreaDetails.get("ata"), Date.from(ataTo.toInstant()));
        }

        if (atdFrom != null) {
            qb.gte(portAreaDetails.get("atd"), Date.from(atdFrom.toInstant()));
        }

        if (atdTo != null) {
            qb.lt(portAreaDetails.get("atd"), Date.from(atdTo.toInstant()));
        }

        return qb.getResults( "portCallId");
    }
}
