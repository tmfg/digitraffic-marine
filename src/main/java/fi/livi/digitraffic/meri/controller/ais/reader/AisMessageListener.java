package fi.livi.digitraffic.meri.controller.ais.reader;

public interface AisMessageListener {
    void receiveMessage(final AisRadioMsg message);
}
