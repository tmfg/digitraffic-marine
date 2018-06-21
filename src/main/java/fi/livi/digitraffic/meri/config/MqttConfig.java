package fi.livi.digitraffic.meri.config;


import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@ConditionalOnProperty("ais.mqtt.enabled")
@Configuration
@IntegrationComponentScan
public class MqttConfig {
    private final String clientId = "marine_updater_" + MqttClient.generateClientId();

    @Bean
    public MqttPahoClientFactory mqttClientFactory(
        @Value("${mqtt.server.url}") final String serverUrl,
        @Value("${mqtt.server.username}") final String username,
        @Value("${mqtt.server.password}")final String password) {
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        factory.getConnectionOptions().setServerURIs(ArrayUtils.toArray(serverUrl));
        factory.getConnectionOptions().setUserName(username);
        factory.getConnectionOptions().setPassword(password.toCharArray());
        factory.getConnectionOptions().setMaxInflight(10000);
        factory.getConnectionOptions().setConnectionTimeout(5);
        factory.getConnectionOptions().setAutomaticReconnect(true);

        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(final MqttPahoClientFactory mqttPahoClientFactory) {
        final MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory);

        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface VesselGateway {
        void sendToMqtt(final Message data);
    }
}
