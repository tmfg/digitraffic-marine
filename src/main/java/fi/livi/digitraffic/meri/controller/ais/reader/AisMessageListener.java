package fi.livi.digitraffic.meri.controller.ais.reader;

import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;

public interface AisMessageListener {
    void receiveMessage(final AisRadioMsg message);
}
