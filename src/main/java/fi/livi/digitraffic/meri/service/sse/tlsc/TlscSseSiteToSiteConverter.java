package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;

@ConditionalOnWebApplication
@Component
public class TlscSseSiteToSiteConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.tlsc.sse.Site, SseSite> {

    private static final Logger log = LoggerFactory.getLogger(TlscSseSiteToSiteConverter.class);

    @Override
    public SseSite convert(fi.livi.digitraffic.meri.external.tlsc.sse.Site src) {
        if (!src.getAdditionalProperties().isEmpty()) {
            log.warn("No mappings at {} for additionalProperties={}", src.getClass(), src.getAdditionalProperties());
        }
        return new SseSite(src.getSiteName(), src.getSiteNumber(), src.getAdditionalProperties());
    }
}
