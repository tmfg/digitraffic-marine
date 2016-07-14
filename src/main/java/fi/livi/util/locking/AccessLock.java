package fi.livi.util.locking;

import java.util.concurrent.atomic.AtomicBoolean;

public class AccessLock {
    private final AtomicBoolean lock;

    public AccessLock(AtomicBoolean lock) {
        this.lock = lock;
    }

    public boolean get() {
        return lock.get();
    }
}
