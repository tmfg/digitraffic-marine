package fi.livi.util.locking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private static final int lockingDuration = 60 * 1000;

    private final LockingRepository lockingRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Autowired
    public LockingService(final LockingRepository lockingRepository, final PlatformTransactionManager platformTransactionManager) {
        this.lockingRepository = lockingRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean tryLock(final String lockName) {
        return lockingRepository.tryAcquireLock(lockName) != null;
    }

    @Transactional(propagation = Propagation.NEVER)
    public AccessLock lock(final String lockName) {
        final AtomicBoolean ab = new AtomicBoolean(false);

        executorService.execute(() -> doLock(lockName, ab));

        return new AccessLock(ab);
    }

    private void doLock(final String lockName, final AtomicBoolean ab) {
        while(true) {
            log.debug("Acquiring lock " + lockName);

            try {
                lockAndInvoke(lockName, ab);
            } catch(final Exception e) {
                log.error(e);
            } finally {
                ab.set(false);

                log.debug("Lock released " + lockName);
            }
        }
    }

    private void lockAndInvoke(final String lockName, final AtomicBoolean ab) {
        final TransactionTemplate template = new TransactionTemplate(platformTransactionManager);

        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                acquireLock(lockName);

                ab.set(true);

                log.debug("Lock acquired " + lockName);

                try {
                    Thread.sleep(lockingDuration);
                } catch (final InterruptedException e) {
                    log.debug(e);
                }
            }
        });
    }

    private void acquireLock(final String lockName) {
        final String locked = lockingRepository.acquireLock(lockName);

        if(!lockName.equals(locked)) {
            log.error("Could not find lock-row for " + lockName);

            throw new IllegalStateException();
        }
    }
}
