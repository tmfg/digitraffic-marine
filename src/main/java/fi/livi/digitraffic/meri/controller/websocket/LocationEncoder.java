package fi.livi.digitraffic.meri.controller.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.model.AISMessage;

public class LocationEncoder implements Encoder.Text<AISMessage> {
    @Override
    public void init(final EndpointConfig config) {}

    @Override
    public String encode(final AISMessage message) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public void destroy() {}

}
