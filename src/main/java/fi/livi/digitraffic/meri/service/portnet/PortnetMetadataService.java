package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.portnet.SsnLocationConverter;
import fi.livi.digitraffic.meri.dao.CodeDescriptionRepository;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.domain.portnet.VesselDetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.CodeDescriptions;
import fi.livi.digitraffic.meri.model.portnet.metadata.FeatureCollectionList;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsService;

@Service
public class PortnetMetadataService {
    private final BerthRepository berthRepository;
    private final PortAreaRepository portAreaRepository;
    private final SsnLocationRepository ssnLocationRepository;

    private final CodeDescriptionRepository codeDescriptionRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final VesselDetailsService vesselDetailsService;

    public PortnetMetadataService(final BerthRepository berthRepository,
                                  final PortAreaRepository portAreaRepository,
                                  final SsnLocationRepository ssnLocationRepository,
                                  final CodeDescriptionRepository codeDescriptionRepository,
                                  final UpdatedTimestampRepository updatedTimestampRepository,
                                  final VesselDetailsService vesselDetailsService) {
        this.berthRepository = berthRepository;
        this.portAreaRepository = portAreaRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.codeDescriptionRepository = codeDescriptionRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.vesselDetailsService = vesselDetailsService;
    }

    @Transactional(readOnly = true)
    public CodeDescriptions listCodeDescriptions() {
        return new CodeDescriptions(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                                codeDescriptionRepository.listAllCargoTypes(),
                                codeDescriptionRepository.listAllVesselTypes(),
                                codeDescriptionRepository.listAllAgentTypes()
                        );
    }

    @Transactional(readOnly = true)
    public FeatureCollectionList listaAllMetadata() {
        return SsnLocationConverter.convert(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                ssnLocationRepository.findAll(),
                portAreaRepository.findAll(),
                berthRepository.findAll()
        );
    }

    @Transactional(readOnly = true)
    public FeatureCollectionList findSsnLocationByLocode(final String locode) {
        final SsnLocation location = ssnLocationRepository.findByLocode(locode);

        if(location == null) {
            throw new ObjectNotFoundException("SsnLocation", locode);
        }

        return SsnLocationConverter.convert(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                Collections.singletonList(location),
                portAreaRepository.findByPortAreaKeyLocode(locode),
                berthRepository.findByBerthKeyLocode(locode)
                );
    }

    @Transactional(readOnly = true)
    public FeatureCollectionList findSsnLocationsByCountry(final String country) {
        // Only Finland has port areas and berths defined
        final boolean isFinland = StringUtils.equals(country, "Finland");

        return SsnLocationConverter.convert(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                ssnLocationRepository.findByCountryIgnoreCase(country),
                isFinland ? portAreaRepository.findAll() : Collections.emptyList(),
                isFinland ? berthRepository.findAll() : Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<VesselDetails> findVesselDetails(final ZonedDateTime from, final String vesselName, final Integer mmsi, final Integer imo,
                                                 final List<String> nationalities, final Integer vesselTypeCode) {
        return vesselDetailsService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
