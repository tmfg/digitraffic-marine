package fi.livi.digitraffic.meri.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.common.service.locking.CachedLockingService;
import fi.livi.digitraffic.common.service.locking.LockingService;


@ConditionalOnExpression("'${config.test}' != 'true'")
@Configuration
public class CachedLockerConfiguration {

    @Bean
    @ConditionalOnExpression("'${ais.mqtt.enabled}' == 'true' or '${ais.reader.enabled}' == 'true'")
    public CachedLockingService aisCachedLockingService(final LockingService lockingService) {
        return lockingService.createCachedLockingServiceObject("AIS_LOCK");
    }
}
