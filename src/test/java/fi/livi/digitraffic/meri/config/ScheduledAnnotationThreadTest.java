package fi.livi.digitraffic.meri.config;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import fi.livi.digitraffic.meri.AbstractTestBase;

@TestPropertySource(properties = { "dt.scheduled.annotation.enabled=true" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // Kill all scheduled jobs after the test
public class ScheduledAnnotationThreadTest extends AbstractTestBase {

    private static final Logger log = getLogger(ScheduledAnnotationThreadTest.class);

    @Value("${dt.scheduled.pool.size}")
    int poolSize = 5;

    private final int job1StartErrorsAfter = 10;
    private final int job2StartErrorsAfter = 20;
    private int count1 = 0;
    private int count2 = 0;
    private int scheduledJob1ErrorCount = 0;
    private int scheduledJob2ErrorCount = 0;

    @Test
    public void checkScheduledJobsRunEvenAfterError() {
        final StopWatch start = StopWatch.createStarted();
        while ( ( (count1 <= (job1StartErrorsAfter + poolSize + 1)) ||
            (count2 <= (job2StartErrorsAfter + poolSize + 1)) )  &&
            start.getTime() < 500) {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Assert that scheduledServices has been running even when there has been errors
        Assertions.assertEquals(5, scheduledJob1ErrorCount);
        Assertions.assertEquals(5, scheduledJob2ErrorCount);
        Assertions.assertTrue(count1 > job1StartErrorsAfter + poolSize);
        Assertions.assertTrue(count2 > job2StartErrorsAfter + poolSize);
        log.info("Test took {} ms", start.getTime());
    }

    @Scheduled(fixedRate = 10)
    public void scheduledJob1() {
        count1++;
        // Ensure errors of pool size
        if (count1 > job1StartErrorsAfter && count1 <= job1StartErrorsAfter + poolSize) {
            scheduledJob1ErrorCount++;
            throw new RuntimeException("scheduledJob1 expected error at run " + count1);
        }
        log.info("scheduledJob1: {}", count1);
    }

    @Scheduled(fixedRate = 10)
    public void scheduledJob2() {
        count2++;
        // Ensure errors of pool size
        if (count2 > job2StartErrorsAfter && count2 <= job2StartErrorsAfter + poolSize) {
            scheduledJob2ErrorCount++;
            Assertions.assertTrue(false, "scheduledJob2 expected error at run " + count2);
        }
        log.info("scheduledJob2: {}", count2);
    }
}