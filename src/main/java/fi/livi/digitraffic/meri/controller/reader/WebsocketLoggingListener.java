package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;

public class WebsocketLoggingListener implements WebsocketListener {
    private final WebsocketStatistics.WebsocketType type;

    public WebsocketLoggingListener(final WebsocketStatistics.WebsocketType type) {
        this.type = type; }

    @Override
    public void receiveMessage(final String message) {
        WebsocketStatistics.readWebsocketStatistics(type);
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        WebsocketStatistics.readWebsocketStatus(type, status);
    }
}