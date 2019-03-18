package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;

public interface AisMessageListener {
    void receiveMessage(final AisRadioMsg message);
}
