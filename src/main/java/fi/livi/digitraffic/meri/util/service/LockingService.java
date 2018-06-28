package fi.livi.digitraffic.meri.util.service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.util.dao.LockingDao;

/**
 * Service for locking.
 *
 * This is stateful, it has the information whether the lock for ais was acquired last time or not, if the acquirement happened within a
 * second.  If the last acquirement was over 1 second ago, check the lock from database.
 *
 * This is done to reduce lock-checking from database, because it happends twice for every ais message.
 *
 */
@Service
public class LockingService {
    private final LockingDao lockingDao;

    private final String instanceId;

    private final AtomicBoolean hasAisLock = new AtomicBoolean(false);
    private final AtomicReference<ZonedDateTime> lastAcquirement = new AtomicReference<>();

    private static final String AIS_LOCK = "AIS";

    @Autowired
    public LockingService(final LockingDao lockingDao) {
        this.lockingDao = lockingDao;
        this.instanceId = UUID.randomUUID().toString();

        setAisLockValue(false);
    }

    public synchronized boolean hasLockForAis() {
        if(lastAcquirement.get().isBefore(ZonedDateTime.now().minusSeconds(1))) {
            setAisLockValue(lockingDao.hasLock(AIS_LOCK, instanceId));
        }
        return hasAisLock.get();
    }

    @Transactional
    public synchronized boolean acquireLockForAis() {
        setAisLockValue(acquireLock(AIS_LOCK, instanceId, 2));

        return hasAisLock.get();
    }

    private void setAisLockValue(final boolean value) {
        hasAisLock.set(value);
        lastAcquirement.set(ZonedDateTime.now());
    }

    private boolean acquireLock(final String lockName, final String callerInstanceId, final int expirationSeconds) {
        return lockingDao.acquireLock(lockName, callerInstanceId, expirationSeconds);
    }
}
