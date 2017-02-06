package fi.livi.digitraffic.meri.controller.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndPointErrorLogger {
    private static final List<String> connectionClosedMessages = Arrays.asList("Broken pipe", "Connection reset by peer", "connection was aborted");

    private EndPointErrorLogger() {
    }

    public static void logError(final Throwable t, final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);

        final String message = t.getMessage();

        if (t instanceof IOException && message != null && connectionClosedMessages.stream().anyMatch(m -> message.contains(m))) {
            logger.info("Established connection was aborted by client. Message: {}", message);
        } else {
            logger.error("Exception", t);
        }
    }
}
