package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.CodeDescriptionRepository;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortsAndBerthsJson;

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
                ssnLocationRepository.findAllLocationsProjectedBy().collect(Collectors.toList()),
                portAreaRepository.findAllProjectedBy().collect(Collectors.toList()),
                berthRepository.findAllProjectedBy().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllCargoTypes().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllVesselTypes().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllAgentTypes().collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public PortsAndBerthsJson findSsnLocationByLocode(final String locode) {
        return new PortsAndBerthsJson(
                updatedTimestampRepository.getLastUpdated(PORT_METADATA.name()),
                ssnLocationRepository.findByLocode(locode),
                portAreaRepository.findByPortAreaKeyLocode(locode).collect(Collectors.toList()),
                berthRepository.findByBerthKeyLocode(locode).collect(Collectors.toList())
                );
    }
}
