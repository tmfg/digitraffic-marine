package fi.livi.digitraffic.meri.controller.websocket;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

public class EndPointErrorLogger {
    private static final List<String> connectionClosedMessages = Arrays.asList("Broken pipe", "Connection reset by peer", "connection was aborted");

    private EndPointErrorLogger() {
    }

    public static void logError(final Logger logger, final Throwable t) {
        if(infoOnly(t)) {
            logger.info("Established connection was aborted by client. Message: {}", t.getMessage());
        } else {
            logger.error("Exception", t);
        }
    }

    private static boolean infoOnly(final Throwable t) {
        final String message = t.getMessage();

        if(t instanceof EOFException) {
            return true;
        }

        return t instanceof IOException && message != null && connectionClosedMessages.stream().anyMatch(m -> message.contains(m));
    }
}
