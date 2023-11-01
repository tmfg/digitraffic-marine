package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationDirwayUpdater;

@DisallowConcurrentExecution
public class WinterNavigationDirwayUpdateJob extends SimpleUpdateJob {

    @Autowired
    private WinterNavigationDirwayUpdater winterNavigationDirwayUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        winterNavigationDirwayUpdater.updateWinterNavigationDirways();
    }
}
