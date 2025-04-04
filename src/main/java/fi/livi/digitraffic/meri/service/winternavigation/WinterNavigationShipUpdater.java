package fi.livi.digitraffic.meri.service.winternavigation;

import fi.livi.digitraffic.common.annotation.NotTransactionalServiceMethod;
import ibnet_baltice_winterships.WinterShips;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnNotWebApplication
public class WinterNavigationShipUpdater {

    private final static Logger log = LoggerFactory.getLogger(WinterNavigationShipUpdater.class);

    private final WinterNavigationClient winterNavigationClient;
    private final UpdaterService updaterService;

    @Autowired
    public WinterNavigationShipUpdater(final WinterNavigationClient winterNavigationClient, final UpdaterService updaterService) {
        this.winterNavigationClient = winterNavigationClient;
        this.updaterService = updaterService;
    }

    /**
     * 1. Get winter navigation ships from an external source
     * 2. Insert / update database
     */
    @NotTransactionalServiceMethod
    public void updateWinterNavigationShips() {
        final StopWatch stopWatch = new StopWatch();
        final WinterShips data;

        try {
            data = winterNavigationClient.getWinterNavigationShips();
        } catch(final Exception e) {
            SoapFaultLogger.logException(log, e);

            return;
        } finally {
            log.info("getting winter navigation ships tookMs={}", stopWatch.getDuration().toMillis());
        }

        updaterService.updateWinterNavigationShips(data);
    }

}
