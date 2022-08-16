/**
 * -----
 * Copyright (C) 2018 Digia
 * -----
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * 2019.02.14: Original work is used here as an base implementation
 */
package fi.livi.digitraffic.meri.controller.ais.reader;

import java.io.IOException;

import fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("ais.reader.enabled")
public class AisTcpSocketClientHealthCheck {
    private static final Logger log = LoggerFactory.getLogger(AisTcpSocketClientHealthCheck.class);

    private final AisTcpSocketClient aisTcpSocketClient;

    @Autowired
    public AisTcpSocketClientHealthCheck(final AisTcpSocketClient aisTcpSocketClient) {
        this.aisTcpSocketClient = aisTcpSocketClient;
    }

    @Scheduled(
        initialDelayString = "${ais.connector.socket.keep-alive-initial-delay}",
        fixedDelayString = "${ais.connector.socket.keep-alive-fixed-delay}"
    )
    public void keepAlive() {
        if (aisTcpSocketClient.isConnected()) {
            // Status ok
            connectionStatus(AisTcpSocketClient.ConnectionStatus.CONNECTED);

            try {
                aisTcpSocketClient.sendKeepAlive();
            } catch (IOException e) {
                log.error("Failed to send Keep-Alive", e);

                connectionStatus(AisTcpSocketClient.ConnectionStatus.CONNECT_FAILURE);
            }
        } else {
            // Connection problem
            connectionStatus(AisTcpSocketClient.ConnectionStatus.CONNECT_FAILURE);

            try {
                aisTcpSocketClient.reconnect();
            } catch (Exception e) {
                log.error("Failed to reconnect", e);
            }
        }
    }

    private void connectionStatus(final AisTcpSocketClient.ConnectionStatus connectionStatus) {
        VesselLoggingListener.readAisConnectionStatus(connectionStatus);
    }
}
