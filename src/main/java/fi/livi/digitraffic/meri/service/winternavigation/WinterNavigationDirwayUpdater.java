package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayPointRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirway;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirwayPointPK;
import ibnet_baltice_waypoints.DirWayPointType;
import ibnet_baltice_waypoints.DirWayType;
import ibnet_baltice_waypoints.DirWaysType;

@Service
public class WinterNavigationDirwayUpdater {

    private final static Logger log = LoggerFactory.getLogger(WinterNavigationDirwayUpdater.class);

    private final WinterNavigationClient winterNavigationClient;
    private final WinterNavigationDirwayRepository winterNavigationDirwayRepository;
    private final WinterNavigationDirwayPointRepository winterNavigationDirwayPointRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationDirwayUpdater(final WinterNavigationClient winterNavigationClient,
                                         final WinterNavigationDirwayRepository winterNavigationDirwayRepository,
                                         final WinterNavigationDirwayPointRepository winterNavigationDirwayPointRepository,
                                         final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationClient = winterNavigationClient;
        this.winterNavigationDirwayRepository = winterNavigationDirwayRepository;
        this.winterNavigationDirwayPointRepository = winterNavigationDirwayPointRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    /**
     * 1. Get winter navigation dirways from an external source
     * 2. Insert / update database
     * @return total number of added or updated dirways
     */
    @Transactional
    public int updateWinterNavigationDirways() {
        final DirWaysType data = winterNavigationClient.getWinterNavigationWaypoints();

        final List<String> names = data.getDirWay().stream().map(d -> d.getName()).collect(Collectors.toList());
        winterNavigationDirwayPointRepository.deleteAllNotIn(names);
        int deletedCount = winterNavigationDirwayRepository.deleteAllNotIn(names);

        final List<WinterNavigationDirway> added = new ArrayList<>();
        final List<WinterNavigationDirway> updated = new ArrayList<>();

        final StopWatch stopWatch = StopWatch.createStarted();
        data.getDirWay().forEach(dirway -> update(dirway, added, updated));
        winterNavigationDirwayRepository.saveAll(added);
        stopWatch.stop();

        log.info("method=updateWinterNavigationDirways addedDirways={} , updatedDirways={} , deletedDirways={} , tookMs={}",
                 added.size(), updated.size(), deletedCount, stopWatch.getTime());

        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_DIRWAYS.name(),
                                              data.getDataValidTime().toGregorianCalendar().toZonedDateTime(),
                                              getClass().getSimpleName());

        return added.size() + updated.size();
    }

    private void update(final DirWayType dirway, final List<WinterNavigationDirway> added, final List<WinterNavigationDirway> updated) {
        final WinterNavigationDirway old = winterNavigationDirwayRepository.findById(dirway.getName()).orElse(null);

        if (old == null) {
            added.add(addNew(dirway));
        } else {
            updated.add(update(old, dirway));
        }
    }

    private WinterNavigationDirway addNew(final DirWayType dirway) {
        final WinterNavigationDirway d = new WinterNavigationDirway();

        updateData(d, dirway);

        return d;
    }

    private WinterNavigationDirway update(final WinterNavigationDirway d, final DirWayType dirway) {
        updateData(d, dirway);

        return d;
    }

    private void updateData(final WinterNavigationDirway d, final DirWayType dirway) {
        d.setName(dirway.getName());
        d.setIssuerCode(dirway.getIssuerCode());
        d.setIssuerName(dirway.getIssuerName());
        d.setIssueTime(WinterNavigationShipUpdater.findZonedDateTime(dirway.getIssueTime()));
        d.setValidUntil(WinterNavigationShipUpdater.findZonedDateTime(dirway.getValidUntil()));

        updateDirwayPoints(d, dirway.getDirWayPoints());
    }

    private void updateDirwayPoints(final WinterNavigationDirway d, final DirWayType.DirWayPoints dirwayPoints) {
        d.getDirwayPoints().clear();
        winterNavigationDirwayPointRepository.deleteInBatch(d.getDirwayPoints());
        winterNavigationDirwayPointRepository.flush();

        if (dirwayPoints == null) {
            return;
        }

        for (final DirWayPointType point : dirwayPoints.getDirWayPoint()) {
            final WinterNavigationDirwayPoint p = new WinterNavigationDirwayPoint();
            p.setWinterNavigationDirwayPointPK(new WinterNavigationDirwayPointPK(d.getName(), point.getId().longValue()));
            p.setLongitude(point.getLon().doubleValue());
            p.setLatitude(point.getLat().doubleValue());
            p.setSeaArea(point.getSeaArea());
            d.getDirwayPoints().add(p);
        }
    }
}
