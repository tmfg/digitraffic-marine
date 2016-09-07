package fi.livi.util.locking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class LockingService {
    private static final Logger log = Logger.getLogger(LockingService.class);

    private static final ExecutorService executorService = Executors.newCachedThreadPool(new CustomizableThreadFactory("executor-"));

    private static final int DEFAULT_LOCKING_DURATION = 1 * 60 * 1000;
    private final int lockingDuration;

    private final LockingRepository lockingRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    @Autowired
    public LockingService(final LockingRepository lockingRepository, final PlatformTransactionManager platformTransactionManager) {
        this.lockingRepository = lockingRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.lockingDuration = DEFAULT_LOCKING_DURATION;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean tryLock(final String lockName) {
        return lockingRepository.tryAcquireLock(lockName) != null;
    }

    @Transactional(propagation = Propagation.NEVER)
    public AccessLock lock(final String lockName) {
        final AtomicInteger count = new AtomicInteger(0);

        executorService.execute(() -> doLock(lockName, count));

        return new AccessLock(count);
    }

    private void doLock(final String lockName, final AtomicInteger count) {
        while(!shutdown.get()) {
            log.debug("Acquiring lock " + lockName);

            try {
                lockAndInvoke(lockName, count);
            } catch(final Exception e) {
                log.error(e);

                sleep(1000);
            } finally {
                log.debug(String.format("Lock %s released %d", lockName, count.get()));
            }
        }
    }

    private void lockAndInvoke(final String lockName, final AtomicInteger count) {
        final TransactionTemplate template = new TransactionTemplate(platformTransactionManager);

        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                acquireLock(lockName);

                final int cc= count.incrementAndGet();
                log.debug(String.format("Lock %s acquired %d", lockName, cc));

                sleep(lockingDuration);

                while(!count.compareAndSet(1, 0)) {
                    log.debug(String.format("waiting for %s %d...", lockName, count.get()));
                    sleep(10);
                }
            }
        });
    }

    private static void sleep(final int duration) {
        try {
            Thread.sleep(duration);
        } catch (final InterruptedException e) {
            log.debug(e);
        }
    }

    private void acquireLock(final String lockName) {
        final String locked = lockingRepository.acquireLock(lockName);

        if(!lockName.equals(locked)) {
            log.error("Could not find lock-row for " + lockName);

            throw new IllegalStateException();
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.debug("destroy");

        executorService.shutdown();
        shutdown.set(true);
    }
}
