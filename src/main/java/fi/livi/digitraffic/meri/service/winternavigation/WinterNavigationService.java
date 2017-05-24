package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortProperties;

@Service
public class WinterNavigationService {

    private final WinterNavigationPortRepository winterNavigationRepository;

    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationService(final WinterNavigationPortRepository winterNavigationRepository,
                                   final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationRepository = winterNavigationRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {

        final List<WinterNavigationPort> ports = winterNavigationRepository.findDistinctByObsoleteDateIsNullOrderByLocode();

        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(WINTER_NAVIGATION_PORTS.name());

        return new WinterNavigationPortFeatureCollection(
            lastUpdated == null ? null : ZonedDateTime.ofInstant(lastUpdated, ZoneId.systemDefault()),
            ports.stream().map(this::portFeature).collect(Collectors.toList()));
    }

    private WinterNavigationPortFeature portFeature(final WinterNavigationPort p) {
        return new WinterNavigationPortFeature(p.getLocode(),
                                               new WinterNavigationPortProperties(p.getName(), p.getNationality(), p.getSeaArea(), p.getPortRestrictions()),
                                               new Point(p.getLongitude(), p.getLatitude()));
    }
}
