package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;

@DisallowConcurrentExecution
public class BerthUpdateJob extends SimpleUpdateJob {
    @Autowired
    private BerthUpdater berthUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        berthUpdater.updatePortsAreasAndBerths();
    }
}
