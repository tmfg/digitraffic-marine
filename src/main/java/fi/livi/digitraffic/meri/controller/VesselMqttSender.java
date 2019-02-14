package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.config.MqttConfig;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@ConditionalOnProperty("ais.mqtt.enabled")
@Component
public class VesselMqttSender {
    private static final Logger LOG = LoggerFactory.getLogger(VesselMqttSender.class);

    @Lazy // this will not be available if mqtt is not enabled
    private final MqttConfig.VesselGateway vesselGateway;
    private final ObjectMapper objectMapper;

    private static final String VESSELS_METADATA_TOPIC = "vessels/%d/metadata";
    private static final String VESSELS_LOCATIONS_TOPIC = "vessels/%d/locations";
    private static final String VESSEL_STATUS_TOPIC  ="vessels/status";

    @Autowired
    public VesselMqttSender(final MqttConfig.VesselGateway vesselGateway, final ObjectMapper objectMapper) {
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

    // This must be synchronized, because Paho does not support concurrency!
    private synchronized void sendMessage(final String payLoad, final String topic) {
        vesselGateway.sendToMqtt(topic, payLoad);
    }

    public void sendNewAisMessage(VesselMetadata meta, VesselLocationFeature location) {
        try {
            String msg = meta != null ? objectMapper.writeValueAsString(meta) : objectMapper.writeValueAsString(location);
            String topic = meta != null ? "vessels/%d/metadata2" : "vessels/%d/locations2";

            sendMessage(msg, topic);
        } catch (Exception e) {
            LOG.error("error sending new message", e);
        }


    }
}
