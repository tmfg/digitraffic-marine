package fi.livi.digitraffic.meri.service.winternavigation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortProperties;

@Service
public class WinterNavigationService {

    private final WinterNavigationRepository winterNavigationRepository;

    @Autowired
    public WinterNavigationService(final WinterNavigationRepository winterNavigationRepository) {
        this.winterNavigationRepository = winterNavigationRepository;
    }

    @Transactional
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {

        final List<WinterNavigationPort> ports = winterNavigationRepository.findDistinctLocodeByObsoleteDateIsNullOrderByLocode();

        return new WinterNavigationPortFeatureCollection(ports.stream().map(this::portFeature).collect(Collectors.toList()));
    }

    private WinterNavigationPortFeature portFeature(final WinterNavigationPort p) {
        return new WinterNavigationPortFeature(p.getLocode(),
                                               new WinterNavigationPortProperties(p.getName(), p.getNationality(), p.getSeaArea(), p.getPortRestrictions()),
                                               new Point(p.getLongitude(), p.getLatitude()));
    }
}
