package fi.livi.digitraffic.meri.service.v2.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.portnet.SsnLocationConverter;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.dao.v2.V2CodeDescriptionRepository;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.LocationFeatureCollections_V1;
import fi.livi.digitraffic.meri.model.v2.portnet.metadata.V2CodeDescriptions;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsService;

@Service
@ConditionalOnWebApplication
public class PortnetMetadataService_V2 {
    private final BerthRepository berthRepository;
    private final PortAreaRepository portAreaRepository;
    private final SsnLocationRepository ssnLocationRepository;

    private final V2CodeDescriptionRepository v2CodeDescriptionRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final VesselDetailsService vesselDetailsService;

    public PortnetMetadataService_V2(final BerthRepository berthRepository,
                                     final PortAreaRepository portAreaRepository,
                                     final SsnLocationRepository ssnLocationRepository,
                                     final V2CodeDescriptionRepository v2CodeDescriptionRepository,
                                     final UpdatedTimestampRepository updatedTimestampRepository,
                                     final VesselDetailsService vesselDetailsService) {
        this.berthRepository = berthRepository;
        this.portAreaRepository = portAreaRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.v2CodeDescriptionRepository = v2CodeDescriptionRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.vesselDetailsService = vesselDetailsService;
    }

    @Transactional(readOnly = true)
    public V2CodeDescriptions listCodeDescriptions() {
        return new V2CodeDescriptions(
                updatedTimestampRepository.findLastUpdated(PORT_METADATA),
                                v2CodeDescriptionRepository.listAllCargoTypes(),
                                v2CodeDescriptionRepository.listAllVesselTypes(),
                                v2CodeDescriptionRepository.listAllAgentTypes()
                        );
    }

    @Transactional(readOnly = true)
    public LocationFeatureCollections_V1 listaAllMetadata() {
        return SsnLocationConverter.convert_V1(
                updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
                ssnLocationRepository.streamAllBy(),
                portAreaRepository.streamAllBy(),
                berthRepository.streamAllBy()
        );
    }

    @Transactional(readOnly = true)
    public LocationFeatureCollections_V1 findSsnLocationByLocode(final String locode) {
        final SsnLocation location = ssnLocationRepository.findByLocode(locode);

        if(location == null) {
            throw new ObjectNotFoundException("SsnLocation", locode);
        }

        return SsnLocationConverter.convert_V1(
                updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
                Stream.of(location),
                portAreaRepository.streamByPortAreaKeyLocode(locode),
                berthRepository.streamByBerthKeyLocode(locode)
                );
    }

    @Transactional(readOnly = true)
    public LocationFeatureCollections_V1 findSsnLocationsByCountry(final String country) {
        // Only Finland has port areas and berths defined
        final boolean isFinland = StringUtils.equals(country, "Finland");

        return SsnLocationConverter.convert_V1(
                updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
                ssnLocationRepository.streamByCountryIgnoreCase(country),
                isFinland ? portAreaRepository.streamAllBy() : Stream.empty(),
                isFinland ? berthRepository.streamAllBy() : Stream.empty());
    }

    @Transactional(readOnly = true)
    public List<VesselDetails> findVesselDetails(final Instant from, final String vesselName, final Integer mmsi, final Integer imo,
                                                 final List<String> nationalities, final Integer vesselTypeCode) {
        return vesselDetailsService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
