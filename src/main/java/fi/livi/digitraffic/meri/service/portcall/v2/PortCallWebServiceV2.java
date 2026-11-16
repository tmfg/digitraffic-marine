package fi.livi.digitraffic.meri.service.portcall.v2;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.*;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortLocationDtoV1;
import fi.livi.digitraffic.meri.model.portnet.Berth;
import fi.livi.digitraffic.meri.model.portnet.PortArea;
import fi.livi.digitraffic.meri.model.portnet.SsnLocation;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.portcall.v1.PortLocationConverterV1;
import jakarta.persistence.EntityManager;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Stream;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;

@Service
@ConditionalOnWebApplication
public class PortCallWebServiceV2 {
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final SsnLocationRepository ssnLocationRepository;
    private final PortAreaRepository portAreaRepository;
    private final BerthRepository berthRepository;

    public PortCallWebServiceV2(final UpdatedTimestampRepository updatedTimestampRepository, final SsnLocationRepository ssnLocationRepository, final PortAreaRepository portAreaRepository, final BerthRepository berthRepository) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.ssnLocationRepository = ssnLocationRepository;
        this.portAreaRepository = portAreaRepository;
        this.berthRepository = berthRepository;
    }

    @Transactional(readOnly = true)
    public PortLocationDtoV1 findPortsLocations(@NonNull final String country) {
        final var isFinland = country.equals("FI");

        // no other than finnish port areas or berths
        final var locations = ssnLocationRepository.findByLocodeStartsWith(country);
        final Stream<PortArea> portAreas = isFinland ? portAreaRepository.streamAllBy() : Stream.empty();
        final Stream<Berth> berths = isFinland ? berthRepository.streamAllBy() : Stream.empty();

        return PortLocationConverterV1.convertV1(
            updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
            locations.stream(), portAreas, berths
        );
    }

    @Transactional(readOnly = true)
    public PortLocationDtoV1 findPortsLocations() {
        final var locations = ssnLocationRepository.findAll();
        final var portAreas =  portAreaRepository.streamAllBy();
        final var berths = berthRepository.streamAllBy();

        return PortLocationConverterV1.convertV1(
            updatedTimestampRepository.findLastUpdatedInstant(PORT_METADATA),
            locations.stream(), portAreas, berths
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
}
