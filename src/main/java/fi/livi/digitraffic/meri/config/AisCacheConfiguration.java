package fi.livi.digitraffic.meri.config;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AisCacheConfiguration implements JCacheManagerCustomizer {

    public static final String ALLOWED_MMSI_CACHE = "allowedMmsis";

    @Override
    public void customize(CacheManager cacheManager) {
        if (cacheManager.getCache(ALLOWED_MMSI_CACHE) == null) {
            cacheManager.createCache(ALLOWED_MMSI_CACHE, new MutableConfiguration<>()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true));
        }
    }
}
