package fi.livi.digitraffic.meri.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.util.service.LockingService;

@ConditionalOnExpression("'${config.test}' != 'true'")
@Configuration
public class CachedLockerConfig {

    @Bean
    @ConditionalOnExpression("'${ais.mqtt.enabled}' == 'true' or '${ais.reader.enabled}' == 'true'")
    public CachedLocker aisCachedLocker(final LockingService lockingService) {
        return new CachedLocker(lockingService, "AIS_LOCK");
    }

    @Bean
    @ConditionalOnProperty("sse.mqtt.enabled")
    public CachedLocker sseCachedLocker(final LockingService lockingService) {
        return new CachedLocker(lockingService, "SSE_LOCK");
    }
}
