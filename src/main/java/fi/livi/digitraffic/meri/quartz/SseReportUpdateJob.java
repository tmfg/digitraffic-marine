package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.service.sse.SseUpdateService;

@DisallowConcurrentExecution
public class SseReportUpdateJob extends SimpleUpdateJob {

    @Autowired
    private SseUpdateService sseUpdateService;

    @Override
    protected void doExecute(JobExecutionContext context) throws Exception {
        sseUpdateService.handleUnhandledSseReports(500);
    }
}
