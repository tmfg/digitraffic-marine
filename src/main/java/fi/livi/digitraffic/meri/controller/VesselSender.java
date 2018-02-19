package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.config.MqttConfig;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@Component
public class VesselSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselSender.class);

    private final MqttConfig.VesselGateway vesselGateway;
    private final ObjectMapper objectMapper;

    @Autowired
    public VesselSender(final MqttConfig.VesselGateway vesselGateway, final ObjectMapper objectMapper) {
        this.vesselGateway = vesselGateway;
        this.objectMapper = objectMapper;
    }

    public void sendLocationMessage(final VesselLocationFeature vesselLocation) {
        try {
            final String locationAsString = objectMapper.writeValueAsString(vesselLocation);
            final MessageBuilder<String> payloadBuilder = MessageBuilder.withPayload(locationAsString);

            final String topic = String.format("vessels");

            final Message<String> message = payloadBuilder.setHeader(MqttHeaders.TOPIC, topic).build();
            vesselGateway.sendToMqtt(message);
        } catch (final Exception e) {
            LOG.error("error sending", e);
        }
    }
}
