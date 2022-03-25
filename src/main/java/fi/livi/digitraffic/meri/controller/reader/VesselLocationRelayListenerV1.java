package fi.livi.digitraffic.meri.controller.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import fi.livi.digitraffic.meri.service.ais.VesselLocationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener.AISLoggingType.POSITION;
import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.AIS_LOCATION;
import static fi.livi.digitraffic.meri.util.MqttUtil.getTopicForMessage;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationRelayListenerV1 implements AisMessageListener {
    private final MqttMessageSender mqttMessageSender;

    private static final String VESSELS_LOCATIONS_V1_TOPIC = "vessels/%d/locations";

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselLocationRelayListenerV1.class);

    public VesselLocationRelayListenerV1(final MqttRelayQueue mqttRelayQueue,
                                         final ObjectMapper objectMapper,
                                         final CachedLocker aisCachedLocker) {
        this.mqttMessageSender = new MqttMessageSender(LOGGER, mqttRelayQueue, objectMapper, AIS_LOCATION, aisCachedLocker);
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && mqttMessageSender.hasLock()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                final VesselLocationFeature mqttMessage = VesselLocationConverter.convert(ais);
                final String topic = getTopicForMessage(VESSELS_LOCATIONS_V1_TOPIC, ais.attributes.mmsi);

                mqttMessageSender.sendMqttMessage(ZonedDateTime.now(), new MqttDataMessageV2(topic, mqttMessage));

                VesselLoggingListener.sentAisMessagesStatistics(POSITION, true);
            }
        }
    }
}
