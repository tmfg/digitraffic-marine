package fi.livi.digitraffic.meri.util.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.util.dao.LockingDao;

@Service
public class LockingService {
    private final LockingDao lockingDao;

    private final String instanceId;

    public LockingService(final LockingDao lockingDao) {
        this.lockingDao = lockingDao;
        this.instanceId = UUID.randomUUID().toString();
    }

    @Transactional
    public boolean acquireLock(final String lockName, final int expirationSeconds) {
        return lockingDao.acquireLock(lockName, instanceId, expirationSeconds);
    }
}
