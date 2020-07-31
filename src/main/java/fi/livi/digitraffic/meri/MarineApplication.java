package fi.livi.digitraffic.meri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.cache.CachesEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.appoptics.AppOpticsMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.atlas.AtlasMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.wavefront.WavefrontMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.web.reactive.WebFluxMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRetry
@SpringBootApplication(exclude = {
    ActiveMQAutoConfiguration.class,
    AppOpticsMetricsExportAutoConfiguration.class,
    ArtemisAutoConfiguration.class,
    AtlasMetricsExportAutoConfiguration.class,
    AuditEventsEndpointAutoConfiguration.class,
    BatchAutoConfiguration.class,
    BeansEndpointAutoConfiguration.class,
    CachesEndpointAutoConfiguration.class,
    CassandraAutoConfiguration.class,
    CassandraDataAutoConfiguration.class,
    CassandraReactiveDataAutoConfiguration.class,
    CassandraReactiveRepositoriesAutoConfiguration.class,
    CassandraRepositoriesAutoConfiguration.class,
    XADataSourceAutoConfiguration.class,
    ThymeleafAutoConfiguration.class,
    WebSocketMessagingAutoConfiguration.class,
    WebSocketReactiveAutoConfiguration.class,
    WavefrontMetricsExportAutoConfiguration.class,
    WebClientAutoConfiguration.class,
    WebFluxAutoConfiguration.class,
    WebFluxMetricsAutoConfiguration.class})
@EnableScheduling
@EnableTransactionManagement
public class MarineApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MarineApplication.class, args);
    }
}
