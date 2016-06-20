package fi.livi.digitraffic.meri.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.model.AISMessage;
import fi.livi.digitraffic.meri.model.VesselMessage;

public class MessageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(MessageConverter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private MessageConverter() {}

    public static AISMessage convertLocation(final String message) {
        return convert(message, AISMessage.class);
    }

    public static VesselMessage convertMetadata(String message) {
        return convert(message, VesselMessage.class);
    }

    private static <T> T convert(final String message, final Class<T> clazz) {
        try {
            return mapper.readValue(message, clazz);
        } catch (IOException e) {
            LOG.error("error while parsing", e);
        }

        return null;
    }
}
