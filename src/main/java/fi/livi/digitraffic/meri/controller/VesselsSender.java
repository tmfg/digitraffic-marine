package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@Component
public class VesselsSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselsSender.class);

    private final SimpMessagingTemplate template;

    @Autowired
    public VesselsSender(final SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendLocationMessage(final VesselLocationFeature message) {
        try {
            template.convertAndSend("/vessels", message);
            template.convertAndSend("/vessels/" + message.mmsi, message);
        } catch (final MessagingException me) {
            LOG.error("error sending", me);
        }
    }

    public void sendMetadataMessage(final VesselMetadata message) {
        try {
            template.convertAndSend("/vessels", message);
            template.convertAndSend("/vessels/" + message.getMmsi(), message);
        } catch (final MessagingException me) {
            LOG.error("error sending", me);
        }
    }
}
