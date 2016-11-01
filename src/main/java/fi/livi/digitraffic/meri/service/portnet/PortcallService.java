package fi.livi.digitraffic.meri.service.portnet;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.model.portnet.PortCallJson;

@Service
public class PortcallService {
    private final PortCallRepository portCallRepository;

    public PortcallService(final PortCallRepository portCallRepository) {
        this.portCallRepository = portCallRepository;
    }

    @Transactional(readOnly = true)
    public List<PortCallJson> listAllPortCalls() {
        return portCallRepository.findAllProjectedBy();
    }
}
