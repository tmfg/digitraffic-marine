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
import fi.livi.digitraffic.meri.controller.websocket.dto.VesselLocationFeatureDto;
import fi.livi.digitraffic.meri.controller.websocket.dto.VesselMetadataDto;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@Component
public class VesselSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselSender.class);

    private final MqttConfig.VesselGateway vesselGateway;
    private final ObjectMapper objectMapper;

    private static final String VESSELS_TOPIC = "vessels";

    @Autowired
    public VesselSender(final MqttConfig.VesselGateway vesselGateway, final ObjectMapper objectMapper) {
        this.vesselGateway = vesselGateway;
        this.objectMapper = objectMapper;
    }

    public void sendMetadataMessage(final VesselMetadata vesselMetadata) {
        try {
            final String metadataAsString = objectMapper.writeValueAsString(new VesselMetadataDto(vesselMetadata));

            sendMessage(metadataAsString, VESSELS_TOPIC);
            sendMessage(metadataAsString, VESSELS_TOPIC + "/" + vesselMetadata.getMmsi());
        } catch (final Exception e) {
            LOG.error("error sending metadata", e);
        }
    }

    public void sendLocationMessage(final VesselLocationFeature vesselLocation) {
        try {
            final String locationAsString = objectMapper.writeValueAsString(new VesselLocationFeatureDto(vesselLocation));

            sendMessage(locationAsString, VESSELS_TOPIC);
            sendMessage(locationAsString, VESSELS_TOPIC + "/" + vesselLocation.mmsi);
        } catch (final Exception e) {
            LOG.error("error sending location", e);
        }
    }

    private void sendMessage(final String payLoad, final String topic) {
        final MessageBuilder<String> payloadBuilder = MessageBuilder.withPayload(payLoad);
        final Message<String> message = payloadBuilder.setHeader(MqttHeaders.TOPIC, topic).build();

        vesselGateway.sendToMqtt(message);
    }
}
