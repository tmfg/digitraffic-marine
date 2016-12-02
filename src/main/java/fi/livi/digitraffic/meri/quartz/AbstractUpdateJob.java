package fi.livi.digitraffic.meri.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;

@DisallowConcurrentExecution
public abstract class AbstractUpdateJob implements Job {

}
