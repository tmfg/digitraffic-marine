package fi.livi.digitraffic.meri.controller.reader;

public interface WebsocketListener {
    void receiveMessage(final String message);

    void connectionStatus(final ReconnectingHandler.ConnectionStatus status);
}
