package fi.livi.digitraffic.meri.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.service.CachedLockerService;
import fi.livi.digitraffic.meri.service.LockingService;

@ConditionalOnExpression("'${config.test}' != 'true'")
@Configuration
public class CachedLockerConfiguration {

    @Bean
    @ConditionalOnExpression("'${ais.mqtt.enabled}' == 'true' or '${ais.reader.enabled}' == 'true'")
    public CachedLockerService aisCachedLocker(final LockingService lockingService) {
        return new CachedLockerService(lockingService, "AIS_LOCK");
    }

    @Bean
    @ConditionalOnProperty("sse.mqtt.enabled")
    public CachedLockerService sseCachedLocker(final LockingService lockingService) {
        return new CachedLockerService(lockingService, "SSE_LOCK");
    }
}
