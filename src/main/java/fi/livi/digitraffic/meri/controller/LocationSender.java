package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.model.AISMessage;

@Component
public class LocationSender {
    private static final Logger LOG = LoggerFactory.getLogger(LocationSender.class);

    private final SimpMessagingTemplate template;

    @Autowired
    public LocationSender(final SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendMessage(final AISMessage message) {
        try {
            template.convertAndSend("/locations", message);
            template.convertAndSend("/locations/" + message.attributes.mmsi, message);
        } catch (final MessagingException me) {
            LOG.error("error sending", me);
        }
    }
}
