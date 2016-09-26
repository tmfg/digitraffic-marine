package fi.livi.util.locking;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * if lockCount = 0 -> this vm does not own the lock
 * if lockCount > 0 -> this vm owns the lock, lockCount equals the number of threads using the lock
 */
public class AccessLock {
    private final AtomicInteger lockCount;

    public AccessLock(final AtomicInteger lockCount) {
        this.lockCount = lockCount;
    }

    public boolean get() {
        return lockCount.getAndUpdate(current -> current == 0 ? 0 : current+1) > 0;
    }

    public void release() {
        lockCount.decrementAndGet();
    }
}
