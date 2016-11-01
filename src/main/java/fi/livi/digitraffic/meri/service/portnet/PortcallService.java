package fi.livi.digitraffic.meri.service.portnet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;

@Service
public class PortcallService {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    public PortcallService(final PortCallRepository portCallRepository,
                           final UpdatedTimestampRepository updatedTimestampRepository) {
        this.portCallRepository = portCallRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional(readOnly = true)
    public PortCallsJson listAllPortCalls() {
        return new PortCallsJson(
            updatedTimestampRepository.getLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name()),
            portCallRepository.findAllProjectedBy()
        );
    }
}
