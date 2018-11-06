package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

@DisallowConcurrentExecution
@ConditionalOnNotWebApplication
public abstract class AbstractUpdateJob implements Job {

}
