package fi.livi.digitraffic.meri.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import fi.livi.digitraffic.meri.util.service.LockingService;

/**
 * Service for locking execution of desired service to single node. This is stateful and it caches the lock state for one second to reduce db queries.
 * Service tries to acquire lock and update cache once a second and returns the latest state from the cache when asked. So every request for lock state
 * won't trigger new db query. This is done to reduce lock-checking from database, because it might happen quite often for some services.
 *
 */

public class CachedLocker {
    private static final Logger log = LoggerFactory.getLogger(CachedLocker.class);
    private static final Set<String> bookedLockNames = new HashSet<>();

    private final LockingService lockingService;
    private final String lockName;
    private final AtomicBoolean hasLock = new AtomicBoolean(false);

    public CachedLocker(final LockingService lockingService, final String lockName) {
        this.lockingService = lockingService;
        if (bookedLockNames.contains(lockName)) {
            throw new IllegalArgumentException(String.format("Lock named %s is already used. Lock name must be unique. Try with another name.", lockName));
        }
        this.lockName = lockName;
        log.info("Created new {}", this.toString());
    }

    public boolean hasLock() {
        return hasLock.get();
    }

    @Scheduled(fixedRate = 1000)
    protected void acquireLock() {
        hasLock.set(lockingService.acquireLock(lockName, 2));
    }

    @Override
    public String toString() {
        return String.format("%s=%s", CachedLocker.class.getSimpleName(), lockName);
    }
}
