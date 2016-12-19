package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsUpdater;

@DisallowConcurrentExecution
public class VesselDetailsUpdateJob extends AbstractUpdateJob {

    private static final Logger log = LoggerFactory.getLogger(PortCallUpdateJob.class);

    @Autowired
    public VesselDetailsUpdater vesselDetailsUpdater;

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) {
        log.info("Quartz VesselDetailsUpdateJob starting");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        vesselDetailsUpdater.update();

        stopWatch.stop();

        log.info("Quartz VesselDetailsUpdateJob ended (took " + stopWatch.getTotalTimeMillis() + " milliseconds)");
    }
}
