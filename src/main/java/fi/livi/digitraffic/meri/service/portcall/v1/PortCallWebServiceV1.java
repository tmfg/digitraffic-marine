package fi.livi.digitraffic.meri.service.portcall.v1;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_CALLS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.CodeDescriptionRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.dto.portcall.v1.call.PortCallJsonV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.call.PortCallsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.code.CodeDescriptionsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortLocationDtoV1;
import fi.livi.digitraffic.meri.model.portnet.PortAreaDetails;
import fi.livi.digitraffic.meri.model.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.SsnLocation;
import fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsService;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;
import fi.livi.digitraffic.meri.util.dao.ShortItemRestrictionUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Join;

@Service
@ConditionalOnWebApplication
public class PortCallWebServiceV1 {
    private final VesselDetailsService vesselDetailsService;

    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final PortCallRepository portCallRepository;

    private final CodeDescriptionRepository codeDescriptionRepository;

    private final SsnLocationRepository ssnLocationRepository;
    private final PortAreaRepository portAreaRepository;
    private final BerthRepository berthRepository;

    private final EntityManager entityManager;

    private static final String PORTCALL_PORTCALL_TIMESTAMP = "portCallTimestamp";

    public PortCallWebServiceV1(final VesselDetailsService vesselDetailsService,
                                final UpdatedTimestampRepository updatedTimestampRepository,
                                final PortCallRepository portCallRepository,
                                final CodeDescriptionRepository codeDescriptionRepository,
                                final SsnLocationRepository ssnLocationRepository,
                                final PortAreaRepository portAreaRepository,
                                final BerthRepository berthRepository,
                                final EntityManager entityManager) {
        this.vesselDetailsService = vesselDetailsService;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallRepository = portCallRepository;
        this.codeDescriptionRepository = codeDescriptionRepository;
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

        final Instant lastUpdated = updatedTimestampRepository.findLastUpdatedInstant(PORT_CALLS);

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

        final List<PortCallJsonV1> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

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

        final Join<PortCall, PortAreaDetails> portAreaDetails = qb.join("portAreaDetails");

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
            updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
            // Cargo info is not published in PortAreaDetailsJson, so don't share metadata for it
            // TODO remove in next api version
            Collections.emptyList(),
            codeDescriptionRepository.listAllVesselTypes(),
            codeDescriptionRepository.listAllAgentTypes()
        );
    }

    @Transactional(readOnly = true)
    public PortLocationDtoV1 findPortsLocations() {
        return PortLocationConverterV1.convertV1(
            updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
            ssnLocationRepository.streamAllBy(),
            portAreaRepository.streamAllBy(),
            berthRepository.streamAllBy()
        );
    }

    @Transactional(readOnly = true)
    public PortLocationDtoV1 findPortLocationByLocode(final String locode) {
        final SsnLocation location = ssnLocationRepository.findByLocode(locode);

        if (location == null) {
            throw new ObjectNotFoundException("PortLocation", locode);
        }

        return PortLocationConverterV1.convertV1(
            updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
            Stream.of(location),
            portAreaRepository.streamByPortAreaKeyLocode(locode),
            berthRepository.streamByBerthKeyLocode(locode)
        );
    }

//    If we would have locations for berths?
//    @Transactional(readOnly = true)
//    public BerthFeatureCollection findBerths() {
//        return SsnLocationConverter.convertBerths(berthRepository.streamAllBy(), updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA));
//    }

    @Transactional(readOnly = true)
    public List<VesselDetails> findVesselDetails(final Instant from, final String vesselName, final Integer mmsi, final Integer imo,
                                                 final List<String> nationalities, final Integer vesselTypeCode) {
        return vesselDetailsService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }

}
