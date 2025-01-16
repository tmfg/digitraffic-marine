package fi.livi.digitraffic.meri.quartz;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.portnet.call.PortCallUpdater;

public class PortCallUpdateJob extends SimpleUpdateJob {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public PortCallUpdater portCallUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) {
        portCallUpdater.update();
    }
}
