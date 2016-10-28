package fi.livi.digitraffic.meri.service.portnet;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.CodeDescriptionRepository;
import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.portnet.PortsAndBerthsJson;

@Service
public class SsnService {
    private final BerthRepository berthRepository;
    private final PortAreaRepository portAreaRepository;
    private final SsnLocationRepository ssnLocationRepository;

    private final CodeDescriptionRepository codeDescriptionRepository;

    public SsnService(final BerthRepository berthRepository,
                      final PortAreaRepository portAreaRepository,
                      final SsnLocationRepository ssnLocationRepository,
                      final CodeDescriptionRepository codeDescriptionRepository) {
        this.berthRepository = berthRepository;
        this.portAreaRepository = portAreaRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.codeDescriptionRepository = codeDescriptionRepository;
    }

    @Transactional(readOnly = true)
    public PortsAndBerthsJson listaAllMetadata() {
        return new PortsAndBerthsJson(
                ssnLocationRepository.streamAllLocations().collect(Collectors.toList()),
                portAreaRepository.streamAllPortArea().collect(Collectors.toList()),
                berthRepository.streamAllBerth().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllCargoTypes().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllVesselTypes().collect(Collectors.toList()),
                codeDescriptionRepository.streamAllAgentTypes().collect(Collectors.toList()));
    }
}
