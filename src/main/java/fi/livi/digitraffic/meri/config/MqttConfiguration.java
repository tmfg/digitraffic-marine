package fi.livi.digitraffic.meri.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@ConditionalOnExpression("('${ais.mqtt.enabled}' == 'true' OR '${sse.mqtt.enabled}' == 'true') AND '${config.test}' != 'true'")
@ConditionalOnNotWebApplication
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class MqttConfiguration {
    private final String clientId = "marine_updater_" + MqttClient.generateClientId();

    @Bean
    public MqttPahoClientFactory mqttClientFactory(
        @Value("${mqtt.server.url}") final String serverUrl,
        @Value("${mqtt.server.username}") final String username,
        @Value("${mqtt.server.password}")final String password) {
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        factory.getConnectionOptions().setServerURIs(serverUrl.split(","));
        factory.getConnectionOptions().setUserName(username);
        factory.getConnectionOptions().setPassword(password.toCharArray());
        factory.getConnectionOptions().setMaxInflight(10000);
        factory.getConnectionOptions().setConnectionTimeout(5);

        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel", async = "true")
    public MessageHandler mqttOutbound(final MqttPahoClientFactory mqttPahoClientFactory) {
        final MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory);

        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel", defaultRequestTimeout = "2000", defaultReplyTimeout = "2000")
    public interface MqttGateway {
        // Paho does not support concurrency, all calls to this must be synchronized!
        void sendToMqtt(@Header(MqttHeaders.TOPIC) final String topic, @Header(MqttHeaders.QOS) final Integer qos, @Payload final String data);
    }
}
