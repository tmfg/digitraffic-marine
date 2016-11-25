package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.CodeDescriptionRepository;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortsAndBerthsJson;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationJson;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;

@Service
public class PortnetMetadataService {
    private final BerthRepository berthRepository;
    private final PortAreaRepository portAreaRepository;
    private final SsnLocationRepository ssnLocationRepository;

    private final CodeDescriptionRepository codeDescriptionRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    public PortnetMetadataService(final BerthRepository berthRepository,
                                  final PortAreaRepository portAreaRepository,
                                  final SsnLocationRepository ssnLocationRepository,
                                  final CodeDescriptionRepository codeDescriptionRepository,
                                  final UpdatedTimestampRepository updatedTimestampRepository) {
        this.berthRepository = berthRepository;
        this.portAreaRepository = portAreaRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.codeDescriptionRepository = codeDescriptionRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional(readOnly = true)
    public PortsAndBerthsJson listaAllMetadata() {
        return new PortsAndBerthsJson(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                ssnLocationRepository.findAllLocationsProjectedBy(),
                portAreaRepository.findAllProjectedBy(),
                berthRepository.findAllProjectedBy(),
                codeDescriptionRepository.listAllCargoTypes(),
                codeDescriptionRepository.listAllVesselTypes(),
                codeDescriptionRepository.listAllAgentTypes()
        );
    }

    @Transactional(readOnly = true)
    public PortsAndBerthsJson findSsnLocationByLocode(final String locode) {
        final SsnLocationJson location = ssnLocationRepository.findByLocode(locode);

        if(location == null) {
            throw new ObjectNotFoundException("SsnLocation", locode);
        }

        return new PortsAndBerthsJson(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                location,
                portAreaRepository.findByPortAreaKeyLocode(locode),
                berthRepository.findByBerthKeyLocode(locode)
                );
    }

    @Transactional(readOnly = true)
    public List<SsnLocationJson> findSsnLocationsByCountry(final String country) {
        return ssnLocationRepository.findByCountryIgnoreCase(country);
    }
}
