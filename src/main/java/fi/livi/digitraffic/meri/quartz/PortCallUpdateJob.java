package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

import fi.livi.digitraffic.meri.service.portnet.PortCallUpdater;

public class PortCallUpdateJob extends SimpleUpdateJob {
    @Autowired
    public PortCallUpdater portCallUpdater;

    @Override
    protected void doExecute(final JobExecutionContext context) throws Exception {
        portCallUpdater.update();

    }
}
