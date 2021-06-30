package fi.livi.digitraffic.meri.util.dao;

import fi.livi.digitraffic.meri.AbstractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockingDaoTest extends AbstractTestBase {
    @Autowired
    private LockingDao lockingDao;

    private static final String LOCKNAME1 = "testLock_1";
    private static final String LOCKNAME2 = "testLock_2";
    private static final String ID1 = "test_id_1";
    private static final String ID2 = "test_id_2";

    @Test
    public void lockOnce() {
        assertTrue(lockingDao.acquireLock(LOCKNAME1, ID1, 10));
        assertTrue(lockingDao.hasLock(LOCKNAME1, ID1));
    }

    @Test
    public void lockTwice() {
        assertTrue(lockingDao.acquireLock(LOCKNAME1, ID1, 10));
        assertTrue(lockingDao.hasLock(LOCKNAME1, ID1));
        assertFalse(lockingDao.hasLock(LOCKNAME1, ID2));

        assertFalse(lockingDao.acquireLock(LOCKNAME1, ID2, 10));
        assertTrue(lockingDao.hasLock(LOCKNAME1, ID1));
        assertFalse(lockingDao.hasLock(LOCKNAME1, ID2));
    }

    @Test
    public void secondLock() {
        assertTrue(lockingDao.acquireLock(LOCKNAME2, ID2, 10));
        assertFalse(lockingDao.hasLock(LOCKNAME1, ID2));
        assertFalse(lockingDao.hasLock(LOCKNAME2, ID1));
    }

    @Test
    public void releaseLock() {
        assertTrue(lockingDao.acquireLock(LOCKNAME1, ID1, 10));
        assertTrue(lockingDao.hasLock(LOCKNAME1, ID1));

        // wrong id, won't release
        lockingDao.releaseLock(LOCKNAME1, ID2);
        assertTrue(lockingDao.hasLock(LOCKNAME1, ID1));

        // correct id
        lockingDao.releaseLock(LOCKNAME1, ID1);
        assertFalse(lockingDao.hasLock(LOCKNAME1, ID1));
    }
}
