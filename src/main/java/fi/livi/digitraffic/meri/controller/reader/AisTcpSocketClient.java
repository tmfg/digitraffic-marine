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
package fi.livi.digitraffic.meri.controller.reader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("ais.reader.enabled")
public class AisTcpSocketClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(AisTcpSocketClient.class);
    private static final Object LOCK = new Object();

    private final AtomicBoolean isConnecting = new AtomicBoolean(false);
    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    private Socket socket;
    private BufferedReader aisReader;
    private Instant lastKeepAliveReply;
    private boolean firstLine = true;

    private final String aisAddress;
    private final Integer aisPort;
    private final String aisUser;
    private final String aisPassword;
    private final Integer socketTimeout;
    private final Integer keepAliveTimeout;

    private final String keepAliveMessage = "$PSTT,01\\r\\n";
    private final String keepAliveResponseMessage = "$PSTT,01*2E";
    private final String logoffMessage = "$PSTT,FF\\r\\n";

    @Autowired
    public AisTcpSocketClient(@Value("${ais.connector.address}") final String aisAddress,
                              @Value("${ais.connector.port}") final Integer aisPort,
                              @Value("${ais.connector.user}") final String aisUser,
                              @Value("${ais.connector.password}") final String aisPassword,
                              @Value("${ais.connector.socket.timeout}") final Integer socketTimeout,
                              @Value("${ais.connector.socket.keep-alive-timeout}") final Integer keepAliveTimeout) {
        this.aisAddress = aisAddress;
        this.aisPort = aisPort;
        this.aisUser = aisUser;
        this.aisPassword = aisPassword;
        this.socketTimeout = socketTimeout;
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public enum ConnectionStatus {
        UNDEFINED, CONNECTED, DISCONNECTED, CONNECT_FAILURE
    }

    public boolean connect() throws IOException {
        if (isConnecting.compareAndSet(false, true)) {
            //log.info("Connecting to {}:{}", aisAddress, aisPort);

            try {
                socket = createSocket();
                aisReader = createAisReader();
                login();
                isConnected.set(true);
                lastKeepAliveReply = null;
            } finally {
                isConnecting.set(false);
            }

            log.info("Connected to {}:{}", aisAddress, aisPort);
        }

        return isConnected();
    }

    protected Socket createSocket() throws IOException {
        Socket socket = new Socket(aisAddress, aisPort);

        socket.setSoTimeout(socketTimeout);
        socket.setKeepAlive(true);

        return socket;
    }

    protected BufferedReader createAisReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String readLine() {
        try {
            String line = aisReader.readLine();

            // quick retry #1
            if (line == null) {
                line = aisReader.readLine();
            }

            if (firstLine) { // to skip proxy related text
                line = trimFirstLine(line);
                firstLine = false;
            }

            if (keepAliveResponseMessage.equals(line)) {
                lastKeepAliveReply = Instant.now();
                line = aisReader.readLine();
            }

            if (line == null) {
                isConnected.set(false); // trigger reconnect
                log.error("Unable to read from socket. Possible invalid credentials? " +
                          "(OR Application startup? This can happen few times on startup!)");
            }

            //log.debug("lineFromAisServer: {}", line);
            return line;
        } catch (IOException e) {
            isConnected.set(false); // trigger reconnect
            log.error("Unable to read from socket.");
        }

        return null;
    }

    private String trimFirstLine(String line) {
        if (line == null) {
            return null;
        }

        int startIndex = line.indexOf("!");

        if (startIndex > 0) {
            //log.info("Skipped: {}", line.substring(0, startIndex - 1));
            line = line.substring(startIndex);
        }

        return line;
    }

    public boolean isConnected() {
        return socket != null
            && !socket.isClosed()
            && socket.isConnected()
            && isConnected.get()
            && (lastKeepAliveReply == null || lastKeepAliveReply.isAfter(Instant.now().minusMillis(keepAliveTimeout))
        );
    }

    // NOTE! This is handled in AisTcpSocketClientHealthCheck
    public void sendKeepAlive() throws IOException {
        log.info("Sending Keep-Alive message");
        send(keepAliveMessage);
    }

    public boolean reconnect() throws Exception {
        log.info("Reconnect to {}:{}", aisAddress, aisPort);
        close();
        return connect();
    }

    @Override
    public void close() throws Exception {
        //log.info("Disconnecting from {}:{}", aisAddress, aisPort);
        logoff();
        socket.close();
        log.info("Disconnected from {}:{}", aisAddress, aisPort);
    }

    private void logoff() throws IOException {
        //log.info("Logging off from {}:{}", aisAddress, aisPort);
        send(logoffMessage);
        log.info("Logged off from {}:{}", aisAddress, aisPort);
    }

    private void login() throws IOException {
        send(String.format("%c%s%c%s%c", 1, aisUser, 0, aisPassword, 0));
    }

    private void send(String s) throws IOException {
        synchronized (LOCK) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes(s);
            dos.flush();
        }
    }
}
