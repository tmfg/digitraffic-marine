package fi.livi.digitraffic.meri.service.nauticalwarning;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.annotation.NotTransactionalServiceMethod;
import fi.livi.digitraffic.meri.model.pooki.PookiFeatureCollection;

@ConditionalOnWebApplication
@Service
public class NauticalWarningService {

    private final String pookiUrl;
    private final RestTemplate restTemplate;

    public enum Status {
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");

        public final String layer;

        Status(final String layer){
            this.layer = layer;
        }

        public static boolean contains(final String needle) {
            for (final Status status : values()) {
                if (StringUtils.equalsIgnoreCase(status.name(), needle)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Autowired
    public NauticalWarningService(@Value("${dt.pooki.url}") final String pookiUrl, final RestTemplate restTemplate) {
        this.pookiUrl = pookiUrl;
        this.restTemplate = restTemplate;
    }

    private PookiFeatureCollection getObjectFromUrl(final String url) {
        return restTemplate.getForObject(url, PookiFeatureCollection.class);
    }

    @NotTransactionalServiceMethod
    @Retryable(maxAttempts = 2)
    public PookiFeatureCollection getResponseFor(final Status status) {
        final String url = getUrlFor(status);
        final PookiFeatureCollection featureCollection = getObjectFromUrl(url);
        featureCollection.setDataUpdatedTime(findLastUpdatedTime(featureCollection));
        return featureCollection;
    }

    private Instant findLastUpdatedTime(final PookiFeatureCollection fc) {
        return fc.getFeatures()
            .stream().map(f -> getMax(f.getProperties().creationTime, f.getProperties().publishingTime))
            .max(Comparator.naturalOrder())
            .orElse(Instant.EPOCH);
    }

    private Instant getMax(final ZonedDateTime...times) {
        return Arrays.stream(times).filter(Objects::nonNull).map(ZonedDateTime::toInstant).max(Comparator.naturalOrder()).orElse(Instant.EPOCH);
    }

    @NotTransactionalServiceMethod
    public String getUrlFor(final Status status) {
        return String.format("%s?crs=EPSG:4326&layer=%s", pookiUrl, status.layer);
    }
}