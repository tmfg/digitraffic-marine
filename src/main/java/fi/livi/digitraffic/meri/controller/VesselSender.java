package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.config.MqttConfig;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@ConditionalOnProperty("ais.mqtt.enabled")
@Component
public class VesselSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselSender.class);

    @Lazy // this will not be available if mqtt is not enabled
    private final MqttConfig.VesselGateway vesselGateway;
    private final ObjectMapper objectMapper;

    private static final String VESSELS_METADATA_TOPIC = "vessels/%d/metadata";
    private static final String VESSELS_LOCATIONS_TOPIC = "vessels/%d/locations";
    private static final String VESSEL_STATUS_TOPIC  ="vessels/status";

    @Autowired
    public VesselSender(final MqttConfig.VesselGateway vesselGateway, final ObjectMapper objectMapper) {
        this.vesselGateway = vesselGateway;
        this.objectMapper = objectMapper;
    }

    public void sendMetadataMessage(final VesselMetadata vesselMetadata) {
        try {
            final String metadataAsString = objectMapper.writeValueAsString(vesselMetadata);

            sendMessage(metadataAsString, String.format(VESSELS_METADATA_TOPIC, vesselMetadata.getMmsi()));
        } catch (final Exception e) {
            LOG.error("error sending metadata", e);
        }
    }

    public void sendLocationMessage(final VesselLocationFeature vesselLocation) {
        try {
            final String locationAsString = objectMapper.writeValueAsString(vesselLocation);

            sendMessage(locationAsString, String.format(VESSELS_LOCATIONS_TOPIC, vesselLocation.mmsi));
        } catch (final Exception e) {
            LOG.error("error sending location", e);
        }
    }

    public void sendStatusMessage(final String status) {
        try {
            final String statusAsString = objectMapper.writeValueAsString(status);

            sendMessage(statusAsString, VESSEL_STATUS_TOPIC);
        } catch (final Exception e) {
            LOG.error("error sending status", e);
        }
    }

    private synchronized void sendMessage(final String payLoad, final String topic) {
        final MessageBuilder<String> payloadBuilder = MessageBuilder.withPayload(payLoad);
        final Message<String> message = payloadBuilder
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.QOS, (Integer)0)
                .build();

        vesselGateway.sendToMqtt(message);
    }
}
