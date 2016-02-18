package fi.livi.digitraffic.meri.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.model.AISMessage;

public class MessageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(MessageConverter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private MessageConverter() {}

    public static AISMessage convert(final String message) {
        try {
            return mapper.readValue(message, AISMessage.class);
        } catch (IOException e) {
            LOG.error("error while parsing", e);
        }

        return null;
    }
}
