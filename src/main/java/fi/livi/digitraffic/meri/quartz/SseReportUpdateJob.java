package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.sse.SseService;

@DisallowConcurrentExecution
public class SseReportUpdateJob extends SimpleUpdateJob {

    @Autowired
    private SseService sseService;

    @Override
    protected void doExecute(JobExecutionContext context) throws Exception {
        sseService.handleUnhandledSseReports(500);
    }
}
