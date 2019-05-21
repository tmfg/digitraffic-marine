package fi.livi.digitraffic.meri.util.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.meri.util.dao.LockingDao;

@Service
public class LockingService {
    private final LockingDao lockingDao;

    private final String instanceId;

    private static final Logger log = LoggerFactory.getLogger(LockingService.class);

    public LockingService(final LockingDao lockingDao) {
        this.lockingDao = lockingDao;
        this.instanceId = UUID.randomUUID().toString();
    }

    public boolean acquireLock(final String lockName, final int expirationSeconds) {
        return lockingDao.acquireLock(lockName, instanceId, expirationSeconds);
    }
}
