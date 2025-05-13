package fi.livi.digitraffic.meri.service.winternavigation;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.common.annotation.NotTransactionalServiceMethod;
import ibnet_baltice_winterships.WinterShips;

@Service
@ConditionalOnNotWebApplication
public class WinterNavigationShipUpdater {

    private final static Logger log = LoggerFactory.getLogger(WinterNavigationShipUpdater.class);

    private final WinterNavigationClient winterNavigationClient;
    private final UpdaterService updaterService;

    @Autowired
    public WinterNavigationShipUpdater(final WinterNavigationClient winterNavigationClient,
                                       final UpdaterService updaterService) {
        this.winterNavigationClient = winterNavigationClient;
        this.updaterService = updaterService;
    }

    /**
     * 1. Get winter navigation ships from an external source
     * 2. Insert / update database
     */
    @NotTransactionalServiceMethod
    public void updateWinterNavigationShips() {
        final WinterShips data;

        final StopWatch stopWatchGet = StopWatch.createStarted();
        final StopWatch stopWatchUpdate = StopWatch.create();
        try {
            try {
                data = winterNavigationClient.getWinterNavigationShips();
            } catch (final Exception e) {
                SoapFaultLogger.logException(log, e, "updateWinterNavigationShips getWinterNavigationShips");
                throw e;
            } finally {
                stopWatchGet.stop();
                log.info("method=updateWinterNavigationShips getting winter navigation ships tookMs={}",
                    stopWatchGet.getDuration().toMillis());
            }

            try {
                stopWatchUpdate.stop();
                updaterService.updateWinterNavigationShips(data);
            } catch (final Exception e) {
                log.error("method=updateWinterNavigationShips update failed", e);
                throw e;
            } finally {
                stopWatchUpdate.stop();
                log.info("method=updateWinterNavigationShips update tookMs={}",
                    stopWatchUpdate.getDuration().toMillis());
            }
        } finally {
            log.info("method=updateWinterNavigationShips summary getTookMs={}  updateTookMs={} tookMs={}",
                stopWatchGet.getDuration().toMillis(), stopWatchUpdate.getDuration().toMillis(),
                stopWatchGet.getDuration().toMillis() + stopWatchUpdate.getDuration().toMillis());
        }
    }

}
