package fi.livi.digitraffic.meri.controller.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.model.AISMessage;

public class LocationEncoder implements Encoder.Text<AISMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(LocationEncoder.class);

    @Override
    public void init(final EndpointConfig config) {
        // no need
    }

    @Override
    public String encode(final AISMessage message) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            LOG.error("Error when encoding", e);

            return "";
        }
    }

    @Override
    public void destroy() {
        // no need
    }

}
