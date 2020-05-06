package fi.livi.digitraffic.meri.quartz;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleUpdateJob extends AbstractUpdateJob {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final void execute(final JobExecutionContext context) {
        final String jobName = StringUtils.capitalize(context.getJobDetail().getKey().getName().replace("Detail", ""));

        log.info("jobType=Quartz jobName={} start", jobName);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            doExecute(context);
        } catch(final Exception e) {
            log.error("jobType=Quartz Exception executing jobName=" + jobName, e);
        }


        stopWatch.stop();

        log.info("jobType=Quartz jobName={} end ( jobTimeMs={} )", jobName, stopWatch.getTime());
    }

    protected abstract void doExecute(final JobExecutionContext context) throws Exception;
}
