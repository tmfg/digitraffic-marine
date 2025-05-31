package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.common.util.TimeUtil.toInstant;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS_CHECK;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.common.annotation.PerformanceMonitor;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayPointRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayRepository;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirway;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayPointPK;
import ibnet_baltice_waypoints.DirWayPointType;
import ibnet_baltice_waypoints.DirWayType;
import ibnet_baltice_waypoints.DirWaysType;

@Service
@ConditionalOnNotWebApplication
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
    // Get varies between 2–120s and update to db 0–10 s so total time is 2–130s
    @PerformanceMonitor(maxWarnExcecutionTime = 100000, maxErrorExcecutionTime = 150000)
    @Transactional
    public int updateWinterNavigationDirways() {
        final DirWaysType data;
        final StopWatch stopWatchGet = StopWatch.createStarted();
        final StopWatch stopWatchUpdate = StopWatch.create();
        try {
            try {
                data = winterNavigationClient.getWinterNavigationWaypoints();
            } catch(final Exception e) {
                SoapFaultLogger.logException(log, e, "updateWinterNavigationDirways");

                return -1;
            } finally {
                stopWatchGet.stop();
            }

            stopWatchUpdate.start();
            final List<String> names = data.getDirWay().stream().map(DirWayType::getName).collect(Collectors.toList());
            final long deletedCount;

            if(names.isEmpty()) {
                deletedCount = winterNavigationDirwayPointRepository.count();
                winterNavigationDirwayRepository.deleteAll();
            } else {
                winterNavigationDirwayPointRepository.deleteAllNotIn(names);
                deletedCount = winterNavigationDirwayRepository.deleteAllNotIn(names);
            }

            final List<WinterNavigationDirway> added = new ArrayList<>();
            final List<WinterNavigationDirway> updated = new ArrayList<>();

            data.getDirWay().forEach(dirway -> update(dirway, added, updated));
            winterNavigationDirwayRepository.saveAll(added);

            updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_DIRWAYS,
                                                  data.getDataValidTime().toGregorianCalendar().toInstant(),
                                                  getClass().getSimpleName());

            final Instant now = Instant.now();
            if (!added.isEmpty() || !updated.isEmpty()) {
                updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_DIRWAYS, now, getClass().getSimpleName());
            }
            updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_DIRWAYS_CHECK, now, getClass().getSimpleName());
            stopWatchUpdate.stop();

            log.info("method=updateWinterNavigationDirways addedDirways={} , updatedDirways={} , deletedDirways={} , tookMs={}", added
                .size(), updated.size(), deletedCount, stopWatchUpdate.getDuration().toMillis());

            return added.size() + updated.size();
        } finally {
            log.info("method=updateWinterNavigationDirways summary getTookMs={}  updateTookMs={} tookMs={}",
                stopWatchGet.getDuration().toMillis(), stopWatchUpdate.getDuration().toMillis(),
                stopWatchGet.getDuration().toMillis() + stopWatchUpdate.getDuration().toMillis());
        }
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
        d.setIssueTime(toInstant(dirway.getIssueTime()));
        d.setValidUntil(toInstant(dirway.getValidUntil()));

        updateDirwayPoints(d, dirway.getDirWayPoints());
    }

    private void updateDirwayPoints(final WinterNavigationDirway d, final DirWayType.DirWayPoints dirwayPoints) {

        final List<WinterNavigationDirwayPointPK> primaryKeys = new ArrayList<>();
        for (final DirWayPointType point : dirwayPoints.getDirWayPoint()) {
            primaryKeys.add(new WinterNavigationDirwayPointPK(d.getName(), point.getId().longValue()));
        }

        final List<WinterNavigationDirwayPoint> toUpdate = d.getDirwayPoints().stream().filter(dp -> primaryKeys.contains(dp.getWinterNavigationDirwayPointPK())).collect(
            Collectors.toList());
        final List<WinterNavigationDirwayPoint> toDelete = d.getDirwayPoints().stream().filter(dp -> !primaryKeys.contains(dp.getWinterNavigationDirwayPointPK())).collect(
            Collectors.toList());
        d.getDirwayPoints().removeAll(toDelete);
        winterNavigationDirwayPointRepository.deleteAllInBatch(toDelete);
        winterNavigationDirwayPointRepository.flush();

        if (dirwayPoints == null) {
            return;
        }

        for (final DirWayPointType point : dirwayPoints.getDirWayPoint()) {
            final WinterNavigationDirwayPointPK newPK = new WinterNavigationDirwayPointPK(d.getName(), point.getId().longValue());
            final Optional<WinterNavigationDirwayPoint> toUpdateMaybe =
                toUpdate.stream().filter(dp -> dp.getWinterNavigationDirwayPointPK().equals(newPK)).findFirst();
            final WinterNavigationDirwayPoint p = toUpdateMaybe.orElseGet(() ->  new WinterNavigationDirwayPoint(newPK));
            p.setLongitude(point.getLon().doubleValue());
            p.setLatitude(point.getLat().doubleValue());
            p.setSeaArea(point.getSeaArea());
            if (toUpdateMaybe.isEmpty()) {
                d.getDirwayPoints().add(p);
            }
        }
    }
}
