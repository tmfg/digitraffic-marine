package fi.livi.digitraffic.meri.util.service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.util.dao.LockingDao;

/**
 * Service for locking.
 *
 * This is stateful, it has the information whether the lock for ais was acquired last time or not.  Service tries to acquire lock once a
 * second.
 *
 * This is done to reduce lock-checking from database, because it happens twice for every ais message.
 *
 */
@Service
public class LockingService {
    private final LockingDao lockingDao;

    private final String instanceId;

    private final AtomicBoolean hasAisLock = new AtomicBoolean(false);

    private static final String AIS_LOCK = "AIS";

    private static final Logger log = LoggerFactory.getLogger(LockingService.class);

    public LockingService(final LockingDao lockingDao) {
        this.lockingDao = lockingDao;
        this.instanceId = UUID.randomUUID().toString();

        hasAisLock.set(false);
    }

    public boolean hasLockForAis() {
        final boolean fromAtomic = hasAisLock.get();
        final boolean fromDb = lockingDao.hasLock(AIS_LOCK, instanceId);

        log.info("lock fromAtomic {} fromDb {}", fromAtomic, fromDb);

        return fromDb;
    }

    @Scheduled(fixedRate = 1000)
    protected void acquireLockForAis() {
        hasAisLock.set(acquireLock(AIS_LOCK, instanceId, 2));
    }

    private boolean acquireLock(final String lockName, final String callerInstanceId, final int expirationSeconds) {
        final boolean acquired = lockingDao.acquireLock(lockName, callerInstanceId, expirationSeconds);

        log.info("acquired " + acquired);

        return acquired;
    }
}
