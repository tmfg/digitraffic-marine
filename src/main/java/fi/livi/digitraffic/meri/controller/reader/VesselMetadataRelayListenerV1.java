package fi.livi.digitraffic.meri.controller.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageListener;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;

import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener.AISLoggingType.METADATA;
import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.AIS_METADATA;
import static fi.livi.digitraffic.meri.util.MqttUtil.getTopicForMessage;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselMetadataRelayListenerV1 implements AisMessageListener {
//    private final MqttMessageSender mqttMessageSender;

    private static final String VESSELS_METADATA_V1_TOPIC = "vessels/%d/metadata";
    public static final String VESSEL_STATUS_V1_TOPIC ="vessels/status";

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselMetadataRelayListenerV1.class);

    public VesselMetadataRelayListenerV1(final MqttRelayQueue mqttRelayQueue,
                                         final ObjectMapper objectMapper,
                                         final CachedLocker aisCachedLocker) {
//        this.mqttMessageSender = new MqttMessageSender(LOGGER, mqttRelayQueue, objectMapper, AIS_METADATA, aisCachedLocker);
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
//        if (message.isMmsiAllowed() && mqttMessageSender.hasLock()) {
//            final VesselMessage vm = AisMessageConverter.convertMetadata(message);
//
//            if (vm.validate()) {
//                final VesselMetadata mqttMessage = new VesselMetadata(vm.vesselAttributes);
//                final String topic = getTopicForMessage(VESSELS_METADATA_V1_TOPIC, vm.vesselAttributes.mmsi);
//                mqttMessageSender.sendMqttMessage(ZonedDateTime.now(), new MqttDataMessageV2(topic, mqttMessage));
//
//                VesselLoggingListener.sentAisMessagesStatistics(METADATA, true);
//            }
//        }
    }
}