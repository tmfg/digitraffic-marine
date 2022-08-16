package fi.livi.digitraffic.meri.controller.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageListener;
import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.mqtt.MqttVesselLocationMessageV2;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener.AISLoggingType.POSITION;
import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.AIS_LOCATION;
import static fi.livi.digitraffic.meri.util.MqttUtil.getTopicForMessage;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationRelayListenerV2 implements AisMessageListener {
    private final MqttMessageSender mqttMessageSender;

    private static final String VESSELS_LOCATIONS_V2_TOPIC = "vessels-v2/%d/location";
    private static final String VESSELS_LOCATIONS_V2_STATUS_TOPIC = "vessels-v2/status";

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselLocationRelayListenerV1.class);

    public VesselLocationRelayListenerV2(final MqttRelayQueue mqttRelayQueue,
                                         final ObjectMapper objectMapper,
                                         final CachedLocker aisCachedLocker) {
        this.mqttMessageSender = new MqttMessageSender(LOGGER, mqttRelayQueue, objectMapper, AIS_LOCATION, aisCachedLocker);
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && mqttMessageSender.hasLock()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                final MqttVesselLocationMessageV2 mqttMessage = new MqttVesselLocationMessageV2(ais);
                final String topic = getTopicForMessage(VESSELS_LOCATIONS_V2_TOPIC, ais.attributes.mmsi);

                mqttMessageSender.sendMqttMessage(ZonedDateTime.now(), new MqttDataMessageV2(topic, mqttMessage));

                VesselLoggingListener.sentAisMessagesStatistics(POSITION, true);
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

