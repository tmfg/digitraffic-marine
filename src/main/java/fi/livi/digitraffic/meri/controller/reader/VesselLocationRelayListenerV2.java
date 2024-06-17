package fi.livi.digitraffic.meri.controller.reader;

import static fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener.AISLoggingType.POSITION;
import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.AIS_LOCATION;

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
import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.dto.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.dto.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.dto.mqtt.MqttVesselLocationMessageV2;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationRelayListenerV2 implements AisMessageListener {
    private final MqttMessageSender mqttMessageSender;

    private static final String VESSELS_LOCATIONS_V2_TOPIC = "vessels-v2/%d/location";
    private static final String VESSELS_LOCATIONS_V2_STATUS_TOPIC = "vessels-v2/status";

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselLocationRelayListenerV2.class);

    public VesselLocationRelayListenerV2(final MqttRelayQueue mqttRelayQueue,
                                         final ObjectMapper objectMapper,
                                         final CachedLockingService aisCachedLockingService) {
        this.mqttMessageSender = new MqttMessageSender(LOGGER, mqttRelayQueue, objectMapper, AIS_LOCATION, aisCachedLockingService);
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && mqttMessageSender.hasLock()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                final MqttVesselLocationMessageV2 mqttMessage = new MqttVesselLocationMessageV2(ais);
                final String topic = MqttUtil.getTopicForMessage(VESSELS_LOCATIONS_V2_TOPIC, ais.attributes.mmsi);

                VesselLoggingListener.sentAisMessagesStatistics(
                        POSITION,
                        mqttMessageSender.sendMqttMessage(Instant.now(), new MqttDataMessageV2(topic, mqttMessage))
                );
            }
        }
    }

    @Scheduled(fixedDelayString = "${mqtt.status.intervalMs}")
    public void sendStatusMessage() {
        if (mqttMessageSender.hasLock()) {
            mqttMessageSender.sendStatusMessageV2(VESSELS_LOCATIONS_V2_STATUS_TOPIC);
        }
    }

}

