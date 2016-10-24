package fi.livi.digitraffic.meri.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.BerthRepository;
import fi.livi.digitraffic.meri.dao.PortAreaRepository;
import fi.livi.digitraffic.meri.dao.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.portnet.PortsAndBerthsJson;

@Service
public class SsnService {
    private final BerthRepository berthRepository;
    private final PortAreaRepository portAreaRepository;
    private final SsnLocationRepository ssnLocationRepository;

    public SsnService(final BerthRepository berthRepository,
                      final PortAreaRepository portAreaRepository,
                      final SsnLocationRepository ssnLocationRepository) {
        this.berthRepository = berthRepository;
        this.portAreaRepository = portAreaRepository;
        this.ssnLocationRepository = ssnLocationRepository;
    }

    @Transactional(readOnly = true)
    public PortsAndBerthsJson listaAllMetadata() {
        return new PortsAndBerthsJson(
                ssnLocationRepository.streamAllLocations().collect(Collectors.toList()),
                portAreaRepository.streamAllPortArea().collect(Collectors.toList()),
                berthRepository.streamAllBerth().collect(Collectors.toList())
        );
    }
}
