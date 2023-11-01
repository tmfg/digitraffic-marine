/**
 * -----
 * Copyright (C) 2018 Digia
 * -----
 * <p>
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * <p>
 * https://joinup.ec.europa.eu/software/page/eupl
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * <p>
 * 2019.02.14: Original work is used here as an base implementation
 */
package fi.livi.digitraffic.meri.controller.ais.reader;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.common.util.ThreadUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@ConditionalOnProperty("ais.reader.enabled")
public class AisMessageReader implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AisMessageReader.class);

    private final AisTcpSocketClient aisTcpSocketClient;
    private String cachedFirstPart = null;
    private final AtomicBoolean readingEnabled = new AtomicBoolean(false);

    private final LinkedBlockingQueue<AisRadioMsg> queue = new LinkedBlockingQueue<>(4096);

    public AisMessageReader(final AisTcpSocketClient aisTcpSocketClient) {
        this.aisTcpSocketClient = aisTcpSocketClient;

        try {
            aisTcpSocketClient.connect();
        } catch (final IOException ioe) {
            log.error("Failed to initialize AIS-connector", ioe);
        }
    }

    public AisRadioMsg getAisRadioMessage() {
        try {
            return queue.take();
        } catch (final InterruptedException e) {
            log.error("Failed to get ais message from queue", e);
            Thread.currentThread().interrupt();
        }

        return null;
    }

    public int getMessageQueueSize() {
        return queue.size();
    }

    @PostConstruct
    public void setUp() {
        readingEnabled.set(true);

        Executors.newSingleThreadExecutor().submit(this);
    }

    @PreDestroy
    public void destroy() {
        log.debug("Shutting down ais reader");
        try {
            readingEnabled.set(false);
            aisTcpSocketClient.close();
        } catch (final Exception e) {
            log.error("Failed to close connection", e);
        }
    }

    @Override
    public void run() {
        while (readingEnabled.get()) {
            if (aisTcpSocketClient.isConnected()) {
                final String rawAisMessage = aisTcpSocketClient.readLine();

                if (rawAisMessage != null) {
                    if (AisRadioMsgParser.isSupportedMessageType(rawAisMessage)) {
                        final boolean multipartMessage = AisRadioMsgParser.isMultipartRadioMessage(rawAisMessage);

                        if (cachedFirstPart != null && !(multipartMessage && AisRadioMsgParser.getPartNumber(rawAisMessage) == 2)) {
                            cachedFirstPart = null;
                        }

                        // TODO! no response, no execption
                        AisRadioMsgParser.validateChecksum(rawAisMessage);

                        if (!multipartMessage || (cachedFirstPart != null && AisRadioMsgParser.getPartNumber(rawAisMessage) == 2)) {
                            // Parse message and deliver result
                            final AisRadioMsg msg = parse(rawAisMessage);

                            if (msg != null) {
                                if (!queue.offer(msg)) {
                                    log.error("AIS message queue is full! Message is ignored");
                                }
                            }
                        } else if (AisRadioMsgParser.getPartNumber(rawAisMessage) == 1) {
                            // cache first part now
                            cachedFirstPart = rawAisMessage;
                        }
                    } else {
                        log.info("Not supported message");
                        if (cachedFirstPart != null) {
                            cachedFirstPart = null;
                        }
                    }
                } else {
                    try {
                        // Do small sleep and continue
                        TimeUnit.SECONDS.sleep(5);
                    } catch (final InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } else {
                // Connection failure -> let reconnection handler handle this
                log.warn("Unable to establish connection to AIS-connector, waiting retry");

                // Do small sleep and continue
                ThreadUtil.delayMs(5000);
            }
        }

        readingEnabled.set(false);
    }

    private AisRadioMsg parse(final String messagePart) {
        final AisRadioMsg msg;

        if (cachedFirstPart == null) {
            msg = AisRadioMsgParser.parseToAisRadioMessage(messagePart);
        } else {
            msg = AisRadioMsgParser.parseToAisRadioMessage(cachedFirstPart, messagePart);
            cachedFirstPart = null;
        }

        return msg;
    }
}
