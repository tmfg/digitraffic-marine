package fi.livi.digitraffic.meri.mqtt;

import java.time.ZonedDateTime;

public class MqttStatusMessageV1 {
    public final String connectionStatus;
    public final int readErrors;
    public final int sendErrors;
    public final ZonedDateTime timeStamp;

    public MqttStatusMessageV1(final String connectionStatus, final int readErrors, final int sendErrors) {
        this.connectionStatus = connectionStatus;
        this.readErrors = readErrors;
        this.sendErrors = sendErrors;

        timeStamp = ZonedDateTime.now();
    }
}
