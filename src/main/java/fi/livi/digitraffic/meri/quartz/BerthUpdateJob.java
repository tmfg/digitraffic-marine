package fi.livi.digitraffic.meri.quartz;

import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;

@DisallowConcurrentExecution
public class BerthUpdateJob extends AbstractUpdateJob {

    private static final Logger log = LoggerFactory.getLogger(BerthUpdateJob.class);

    @Autowired
    private BerthUpdater berthUpdater;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Quartz BerthUpdateJob starting");

        final StopWatch stopWatch = StopWatch.createStarted();

        try {
            berthUpdater.updatePortsAreasAndBerths();
        } catch (IOException e) {
            log.info("BerthUpdateJob failed", e);
        }

        stopWatch.stop();

        log.info("Quartz BerthUpdateJob ended (took {} milliseconds)", stopWatch.getTime());
    }
}
