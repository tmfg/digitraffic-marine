package fi.livi.digitraffic.meri.controller.reader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketLoggingListener implements WebsocketListener {
    private final AtomicInteger messageCount = new AtomicInteger();
    private final String domain;

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final Logger log = LoggerFactory.getLogger(WebsocketLoggingListener.class);

    public WebsocketLoggingListener(final String domain) {
        executor.scheduleAtFixedRate(this::logMessageCount, 0, 1, TimeUnit.MINUTES);

        this.domain = domain;
    }

    private void logMessageCount() {
        final int callCount = messageCount.getAndSet(0);

        log.info(String.format("Messages received for %s : %d", domain, callCount));
    }

    @Override
    public void receiveMessage(final String message) {
        messageCount.incrementAndGet();
    }
}
