package fi.livi.digitraffic.meri.controller.reader;

import static fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener.AISLoggingType.METADATA;
import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.AIS_METADATA;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.common.service.locking.CachedLockingService;
import fi.livi.digitraffic.common.util.MqttUtil;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageListener;
import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.dto.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.dto.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.dto.mqtt.MqttVesselMetadataMessageV2;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselMetadataRelayListenerV2 implements AisMessageListener {
    private final MqttMessageSender mqttMessageSender;

    private static final String VESSELS_METADATA_V2_TOPIC = "vessels-v2/%d/metadata";
    public static final String VESSEL_STATUS_V2_TOPIC ="vessels-v2/status";

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselMetadataRelayListenerV2.class);

    public VesselMetadataRelayListenerV2(final MqttRelayQueue mqttRelayQueue,
                                         final ObjectMapper objectMapper,
                                         final CachedLockingService aisCachedLockingService) {
        this.mqttMessageSender = new MqttMessageSender(LOGGER, mqttRelayQueue, objectMapper, AIS_METADATA, aisCachedLockingService);
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && mqttMessageSender.hasLock()) {
            final VesselMessage vm = AisMessageConverter.convertMetadata(message);

            if (vm.validate()) {
                final MqttVesselMetadataMessageV2 mqttMessage = new MqttVesselMetadataMessageV2(vm.vesselAttributes);
                final String topic = MqttUtil.getTopicForMessage(VESSELS_METADATA_V2_TOPIC, vm.vesselAttributes.mmsi);
                mqttMessageSender.sendMqttMessage(Instant.now(), new MqttDataMessageV2(topic, mqttMessage));

                VesselLoggingListener.sentAisMessagesStatistics(METADATA, true);
            }
        }
    }

    @Scheduled(fixedDelayString = "${mqtt.status.intervalMs}")
    public void sendStatusMessage() {
        if (mqttMessageSender.hasLock()) {
            mqttMessageSender.sendStatusMessageV2(VESSEL_STATUS_V2_TOPIC);
        }
    }
}