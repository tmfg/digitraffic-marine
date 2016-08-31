package fi.livi.digitraffic.meri.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ConditionalOnExpression("'${config.test}' != 'true'")
@Configuration
public class ServerEndpointConfig {
    /* Normal websocket endpoint exporter */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
