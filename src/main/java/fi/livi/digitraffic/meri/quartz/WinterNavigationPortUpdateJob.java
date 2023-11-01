package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationPortUpdater;

@DisallowConcurrentExecution
public class WinterNavigationPortUpdateJob extends SimpleUpdateJob {

    @Autowired
    public WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        winterNavigationPortUpdater.updateWinterNavigationPorts();
    }
}