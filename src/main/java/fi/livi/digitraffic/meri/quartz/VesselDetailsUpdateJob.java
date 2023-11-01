package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsUpdater;

@DisallowConcurrentExecution
public class VesselDetailsUpdateJob extends SimpleUpdateJob {
    @Autowired
    public VesselDetailsUpdater vesselDetailsUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        vesselDetailsUpdater.update();
    }
}
