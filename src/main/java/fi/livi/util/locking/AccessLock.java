package fi.livi.util.locking;

import java.util.concurrent.atomic.AtomicInteger;

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
