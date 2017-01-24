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
public class VesselSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselSender.class);

    private final SimpMessagingTemplate template;

    @Autowired
    public VesselSender(final SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendLocationMessage(final VesselLocationFeature message) {
        try {
            template.convertAndSend("/locations", message);
            template.convertAndSend("/locations/" + message.mmsi, message);
        } catch (final MessagingException me) {
            LOG.error("error sending", me);
        }
    }

    public void sendMetadataMessage(final VesselMetadata message) {
        try {
            template.convertAndSend("/locations", message);
            template.convertAndSend("/locations/" + message.getMmsi(), message);
        } catch (final MessagingException me) {
            LOG.error("error sending", me);
        }
    }
}
