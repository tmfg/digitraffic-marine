package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;

@DisallowConcurrentExecution
public class SsnLocationUpdateJob extends SimpleUpdateJob {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) throws Exception {
        ssnLocationUpdater.updateSsnLocations();
    }
}
