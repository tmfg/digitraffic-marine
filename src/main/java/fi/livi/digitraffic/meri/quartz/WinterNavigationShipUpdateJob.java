package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationShipUpdater;

@DisallowConcurrentExecution
public class WinterNavigationShipUpdateJob extends SimpleUpdateJob {

    @Autowired
    public WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        winterNavigationShipUpdater.updateWinterNavigationShips();
    }
}