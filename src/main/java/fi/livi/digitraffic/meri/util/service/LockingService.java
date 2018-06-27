package fi.livi.digitraffic.meri.util.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.util.dao.LockingDao;

@Service
public class LockingService {
    private final LockingDao lockingDao;

    private final String instanceId;

    private static final String AIS_LOCK = "AIS";

    @Autowired
    public LockingService(final LockingDao lockingDao) {
        this.lockingDao = lockingDao;
        this.instanceId = UUID.randomUUID().toString();
    }

    public boolean hasLockForAis() {
        return lockingDao.hasLock(AIS_LOCK, instanceId);
    }

    @Transactional
    public boolean acquireLockForAis() {
        return acquireLock(AIS_LOCK, instanceId, 2);
    }

    @Transactional
    public boolean acquireLock(final String lockName, final String callerInstanceId, final int expirationSeconds) {
        return lockingDao.acquireLock(lockName, callerInstanceId, expirationSeconds);
    }

    @Transactional
    public void releaseLock(final String lockName, final String callerInstanceId) {
        lockingDao.releaseLock(lockName, callerInstanceId);
    }

}
