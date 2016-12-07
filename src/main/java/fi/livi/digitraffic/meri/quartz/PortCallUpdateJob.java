package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import fi.livi.digitraffic.meri.service.portnet.PortCallUpdater;

@DisallowConcurrentExecution
public class PortCallUpdateJob extends AbstractUpdateJob {

    private static final Logger log = LoggerFactory.getLogger(PortCallUpdateJob.class);

    @Autowired
    public PortCallUpdater portCallUpdater;

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) {
        log.info("Quartz PortCallUpdateJob starting");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        portCallUpdater.update();

        stopWatch.stop();

        log.info("Quartz PortCallUpdateJob ended (took " + stopWatch.getTotalTimeMillis() + " milliseconds)");
    }
}