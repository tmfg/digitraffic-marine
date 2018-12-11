package fi.livi.digitraffic.meri.service.portnet.berth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.meri.dao.portnet.BerthRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortAreaRepository;
import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.BerthKey;
import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaKey;

@Service
@ConditionalOnNotWebApplication
public class BerthUpdater {
    private final PortAreaRepository portAreaRepository;
    private final BerthRepository berthRepository;
    private final BerthClient berthClient;
    private final PortAreaCoordinateReader portAreaCoordinateReader;

    private static final Logger log = LoggerFactory.getLogger(BerthUpdater.class);

    public BerthUpdater(final PortAreaRepository portAreaRepository,
                        final BerthRepository berthRepository, final BerthClient berthClient,
                        final PortAreaCoordinateReader portAreaCoordinateReader) {
        this.portAreaRepository = portAreaRepository;
        this.berthRepository = berthRepository;
        this.berthClient = berthClient;
        this.portAreaCoordinateReader = portAreaCoordinateReader;
    }

    @Transactional
    public void updatePortsAreasAndBerths() throws IOException {
        final List<PortArea> oldPortAreas = portAreaRepository.findAll();
        final List<Berth> oldBerths = berthRepository.findAll();
        final List<BerthLine> berthLines = berthClient.getBerthLines();

        final boolean portAreasMerged = mergePortAreas(oldPortAreas, berthLines);
        mergeBerths(oldBerths, berthLines);

        if(portAreasMerged) {
            updatePortAreaCoordinates(oldPortAreas);
        }
    }

    private void updatePortAreaCoordinates(final List<PortArea> oldPortAreas) {
        final Map<PortAreaKey, PortArea> oldMap = oldPortAreas.stream().collect(Collectors.toMap(PortArea::getPortAreaKey, Function.identity()));
        final List<PortArea> newCoordinates = portAreaCoordinateReader.readCoordinates();

        newCoordinates.stream().forEach(nc -> {
            final PortArea portArea = oldMap.get(nc.getPortAreaKey());

            if(portArea == null) {
                log.error("Can't find portArea={}", nc.getPortAreaKey());
            } else {
                portArea.setWgs84Lat(nc.getWgs84Lat());
                portArea.setWgs84Long(nc.getWgs84Long());
            }
        });
    }

    private void mergeBerths(final List<Berth> oldBerths, final List<BerthLine> berthLines) {
        final Map<BerthKey, Berth> oldMap = oldBerths.stream().collect(Collectors.toMap(Berth::getBerthKey, Function.identity()));
        final List<Berth> newBerths = new ArrayList<>();
        final Set<BerthKey> newKeys = new HashSet<>();
        int updates = 0;

        for(final BerthLine bl : berthLines) {
            final BerthKey bk = BerthKey.of(bl.portCode, bl.portAreaCode, bl.berthCode);
            final Berth newBerth = convert(bk, bl);

            if(newKeys.contains(bk)) {
                log.info(String.format("Duplicate key %s,%s,%s", bk.getLocode(), bk.getPortAreaCode(), bk.getBerthCode()));
            } else {
                newKeys.add(bk);

                if (!oldMap.containsKey(bk)) {
                    newBerths.add(newBerth);
                } else {
                    if(mergeBerth(oldMap.get(newBerth.getBerthKey()), newBerth)) {
                        updates++;
                    }
                }
            }

            oldMap.remove(bk);
        }

        berthRepository.deleteAll(oldMap.values());
        berthRepository.saveAll(newBerths);

        log.info("Read berthsCount={} berths, berthsAddedCount={} berthsUpdatedCount={} berthsDeletedCount={} .",
                berthLines.size(), newBerths.size(), updates, oldMap.values().size());
    }

    private static boolean mergeBerth(final Berth oldBerth, final Berth newBerth) {
        final boolean difference = !StringUtils.equals(oldBerth.getBerthName(), newBerth.getBerthName());

        oldBerth.setBerthName(newBerth.getBerthName());

        return difference;
    }

    private static Berth convert(final BerthKey bk, final BerthLine bl) {
        final Berth berth = new Berth();

        berth.setBerthKey(bk);
        berth.setBerthName(bl.berthName);

        return berth;
    }

    private boolean mergePortAreas(final List<PortArea> oldPortAreas, final List<BerthLine> berthLines) {
        final Map<PortAreaKey, PortArea> oldMap = oldPortAreas.stream().collect(Collectors.toMap(PortArea::getPortAreaKey, Function.identity()));
        final Map<PortAreaKey, BerthLine> portMap =
                berthLines.stream().collect(Collectors.toMap(x -> PortAreaKey.of(x.portCode, x.portAreaCode), Function.identity(), (p1, p2) -> p2));
        final List<PortArea> newAreas = new ArrayList<>();
        int updates = 0;

        for(final Map.Entry<PortAreaKey, BerthLine> e : portMap.entrySet()) {
            final PortArea newArea = convert(e.getKey(), e.getValue());
            if(!oldMap.containsKey(e.getKey())) {
                newAreas.add(newArea);
            } else if(mergePortArea(oldMap.get(e.getKey()), newArea)) {
                updates++;
            }

            oldMap.remove(e.getKey());
        }

        portAreaRepository.deleteAll(oldMap.values());
        portAreaRepository.saveAll(newAreas);

        log.info("portAreasAddedCount={} port areas, portAreasUpdatedCount={} portAreasDeletedCount={} .", newAreas.size(), updates, oldMap.values().size());

        return CollectionUtils.isNotEmpty(newAreas) || updates > 0;
    }

    private static boolean mergePortArea(final PortArea oldArea, final PortArea newArea) {
        final boolean difference = !StringUtils.equals(oldArea.getPortAreaName(), newArea.getPortAreaName());

        oldArea.setPortAreaName(newArea.getPortAreaName());

        return difference;
    }

    private static PortArea convert(final PortAreaKey key, final BerthLine value) {
        final PortArea newPortArea = new PortArea();

        newPortArea.setPortAreaKey(key);
        newPortArea.setPortAreaName(value.portAreaName);

        return newPortArea;
    }

}
