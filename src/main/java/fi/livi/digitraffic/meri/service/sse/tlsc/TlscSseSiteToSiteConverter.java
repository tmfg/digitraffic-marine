package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.domain.sse.tlsc.ExtraFields;
import fi.livi.digitraffic.meri.domain.sse.tlsc.Site;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.external.sse.SSEReport;

@ConditionalOnWebApplication
@Component
public class TlscSseSiteToSiteConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.sse.Site, Site> {

    @Override
    public Site convert(fi.livi.digitraffic.meri.external.sse.Site source) {
        return new Site(source.getSiteName(), source.getSiteNumber(), source.getAdditionalProperties());
    }
}
