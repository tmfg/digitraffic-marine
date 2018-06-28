package fi.livi.digitraffic.meri.config;

import java.util.concurrent.TimeUnit;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AisCacheConfiguration implements JCacheManagerCustomizer {
    public static final String CACHE_ALLOWED_MMSI = "allowedMmsis";

    private final long allowedMmsisSeconds;

    @Autowired
    public AisCacheConfiguration(@Value("${cache.allowedMmsis}") final long allowedMmsisSeconds) {
        this.allowedMmsisSeconds = allowedMmsisSeconds;
    }

    @Override
    public void customize(final CacheManager cacheManager) {
        createCacheIfNeeded(cacheManager, CACHE_ALLOWED_MMSI, allowedMmsisSeconds);
    }

    private void createCacheIfNeeded(final CacheManager cacheManager, final String cacheName, final long duration) {
        if (cacheManager.getCache(cacheName) == null) {
            cacheManager.createCache(cacheName, new MutableConfiguration<>()
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, duration)))
                .setStoreByValue(false)
                .setStatisticsEnabled(true));
        }
    }
}
