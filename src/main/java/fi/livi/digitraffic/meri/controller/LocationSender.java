package fi.livi.digitraffic.meri.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.model.AISMessage;

@Component
public class LocationSender {
    private static final Logger LOG = LoggerFactory.getLogger(LocationSender.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpMessagingTemplate template;

    @Autowired
    public LocationSender(final SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendMessage(final String message) {
        try {
            final AISMessage ais = mapper.readValue(message, AISMessage.class);

            template.convertAndSend("/locations", ais);
            template.convertAndSend("/locations/" + ais.attributes.mmsi, ais);
        } catch (MessagingException me) {
            LOG.error("error sending", me);
        } catch (IOException e) {
            LOG.error("error parsing json", e);
        }
    }
}
