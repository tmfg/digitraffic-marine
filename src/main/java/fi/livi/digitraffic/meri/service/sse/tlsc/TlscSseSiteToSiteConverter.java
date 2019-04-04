package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseSite;

@ConditionalOnWebApplication
@Component
public class TlscSseSiteToSiteConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.tlsc.sse.Site, SseSite> {

    @Override
    public SseSite convert(fi.livi.digitraffic.meri.external.tlsc.sse.Site source) {
        return new SseSite(source.getSiteName(), source.getSiteNumber(), source.getAdditionalProperties());
    }
}
