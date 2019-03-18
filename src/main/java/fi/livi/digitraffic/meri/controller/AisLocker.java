package fi.livi.digitraffic.meri.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.meri.util.service.LockingService;

/**
 * Service for locking ais-messages.
 *
 * This is stateful, it has the information whether the lock for ais was acquired last time or not.  Service tries to acquire lock once a
 * second.
 *
 * This is done to reduce lock-checking from database, because it happens twice for every ais message.
 *
 */

@Service
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class AisLocker {
    private final LockingService lockingService;

    private final AtomicBoolean hasAisLock = new AtomicBoolean(false);

    private static final String AIS_LOCK = "AIS";

    public AisLocker(final LockingService lockingService) {
        this.lockingService = lockingService;
    }

    public boolean hasLockForAis() {
        return hasAisLock.get();
    }

    @Scheduled(fixedRate = 1000)
    protected void acquireLockForAis() {
        hasAisLock.set(lockingService.acquireLock(AIS_LOCK, 2));
    }

}
