package fi.livi.digitraffic.meri.service.portcall;

import fi.livi.digitraffic.meri.controller.portnet.SsnLocationConverter;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.dao.v2.V2CodeDescriptionRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.dto.portcall.v1.CodeDescriptionsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.LocationFeatureCollectionsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.PortCallsV1;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsService;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;
import fi.livi.digitraffic.meri.util.dao.ShortItemRestrictionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_CALLS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@ConditionalOnWebApplication
public class PortCallServiceV1 {
    private final VesselDetailsService vesselDetailsService;

    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final PortCallRepository portCallRepository;

    private final V2CodeDescriptionRepository v2CodeDescriptionRepository;

    private final SsnLocationRepository ssnLocationRepository;
    private final PortAreaRepository portAreaRepository;
    private final BerthRepository berthRepository;

    private final EntityManager entityManager;

    private static final String PORTCALL_PORTCALL_TIMESTAMP = "portCallTimestamp";

    public PortCallServiceV1(final VesselDetailsService vesselDetailsService,
                             final UpdatedTimestampRepository updatedTimestampRepository,
                             final PortCallRepository portCallRepository,
                             final V2CodeDescriptionRepository v2CodeDescriptionRepository,
                             final SsnLocationRepository ssnLocationRepository,
                             final PortAreaRepository portAreaRepository,
                             final BerthRepository berthRepository,
                             final EntityManager entityManager) {
        this.vesselDetailsService = vesselDetailsService;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallRepository = portCallRepository;
        this.v2CodeDescriptionRepository = v2CodeDescriptionRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.portAreaRepository = portAreaRepository;
        this.berthRepository = berthRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public PortCallsV1 findPortCalls(
        final Date modifiedDate,
        final Instant modifiedFrom,
        final Instant modifiedTo,
        final Instant etaFrom,
        final Instant etaTo,
        final Instant etdFrom,
        final Instant etdTo,
        final Instant ataFrom,
        final Instant ataTo,
        final Instant atdFrom,
        final Instant atdTo,
        final String locode,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        final Instant lastUpdated = updatedTimestampRepository.findLastUpdated(PORT_CALLS).toInstant();

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
            return new PortCallsV1(lastUpdated, Collections.emptyList());
        }

        if (portCallIds.size() > 1000) {
            throw new BadRequestException("The search result is too big (over 1000 items), try to narrow down your search criteria.");
        }

        final List<PortCallJson> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

        return new PortCallsV1(lastUpdated, portCallList);
    }

    private List<Long> getPortCallIds(
        final Date modifiedDate,
        final Instant modifiedFrom,
        final Instant modifiedTo,
        final Instant etaFrom,
        final Instant etaTo,
        final Instant etdFrom,
        final Instant etdTo,
        final Instant ataFrom,
        final Instant ataTo,
        final Instant atdFrom,
        final Instant atdTo,
        final String locode,
        final String vesselName,
        final Integer mmsi,
        final Integer imo,
        final List<String> nationality,
        final Integer vesselTypeCode) {

        final QueryBuilder<Long, PortCall> qb = new QueryBuilder<>(entityManager, Long.class, PortCall.class);

        if (modifiedDate != null) {
            qb.gte(qb.<Timestamp>get(PORTCALL_PORTCALL_TIMESTAMP), modifiedDate);
            qb.lt(qb.<Timestamp>get(PORTCALL_PORTCALL_TIMESTAMP), DateUtils.addDays(modifiedDate, 1));
        }
        if (modifiedFrom != null) {
            qb.gte(qb.get(PORTCALL_PORTCALL_TIMESTAMP), Date.from(modifiedFrom));
        }
        if (modifiedTo != null) {
            qb.lt(qb.get(PORTCALL_PORTCALL_TIMESTAMP), Date.from(modifiedTo));
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
            qb.gte(portAreaDetails.get("eta"), Date.from(etaFrom));
        }

        if (etaTo != null) {
            qb.lt(portAreaDetails.get("eta"), Date.from(etaTo));
        }

        if (etdFrom != null) {
            qb.gte(portAreaDetails.get("etd"), Date.from(etdFrom));
        }

        if (etdTo != null) {
            qb.lt(portAreaDetails.get("etd"), Date.from(etdTo));
        }

        if (ataFrom != null) {
            qb.gte(portAreaDetails.get("ata"), Date.from(ataFrom));
        }

        if (ataTo != null) {
            qb.lt(portAreaDetails.get("ata"), Date.from(ataTo));
        }

        if (atdFrom != null) {
            qb.gte(portAreaDetails.get("atd"), Date.from(atdFrom));
        }

        if (atdTo != null) {
            qb.lt(portAreaDetails.get("atd"), Date.from(atdTo));
        }

        return qb.getResults( "portCallId");
    }

    @Transactional(readOnly = true)
    public CodeDescriptionsV1 listCodeDescriptions() {
        return new CodeDescriptionsV1(
            updatedTimestampRepository.findLastUpdated(PORT_METADATA).toInstant(),
            v2CodeDescriptionRepository.listAllCargoTypes(),
            v2CodeDescriptionRepository.listAllVesselTypes(),
            v2CodeDescriptionRepository.listAllAgentTypes()
        );
    }

    @Transactional(readOnly = true)
    public LocationFeatureCollectionsV1 listaAllMetadata() {
        return SsnLocationConverter.convertV1(
            updatedTimestampRepository.findLastUpdated(PORT_METADATA),
            ssnLocationRepository.streamAllBy(),
            portAreaRepository.streamAllBy(),
            berthRepository.streamAllBy()
        );
    }

    @Transactional(readOnly = true)
    public LocationFeatureCollectionsV1 findSsnLocationByLocode(final String locode) {
        final SsnLocation location = ssnLocationRepository.findByLocode(locode);

        if(location == null) {
            throw new ObjectNotFoundException("SsnLocation", locode);
        }

        return SsnLocationConverter.convertV1(
            updatedTimestampRepository.findLastUpdated(PORT_METADATA),
            Stream.of(location),
            portAreaRepository.streamByPortAreaKeyLocode(locode),
            berthRepository.streamByBerthKeyLocode(locode)
        );
    }

    @Transactional(readOnly = true)
    public List<VesselDetails> findVesselDetails(final Instant from, final String vesselName, final Integer mmsi, final Integer imo,
                                                 final List<String> nationalities, final Integer vesselTypeCode) {
        return vesselDetailsService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
