package fi.livi.digitraffic.meri.controller.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndPointErrorLogger {

    private EndPointErrorLogger() {
    }

    private static final List<String> connectionClosedMessages = Arrays.asList("Broken pipe", "Connection reset by peer", "connection was aborted");

    public static void logError(final Throwable t, Class clazz) {

        final Logger LOG = LoggerFactory.getLogger(clazz);

        final String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();

        if (t instanceof IOException && connectionClosedMessages.stream().anyMatch(m -> message.contains(m))) {
            LOG.info("Established connection was aborted by client. Message: {}", message);
        } else {
            LOG.error("Exception", t);
        }
    }
}
