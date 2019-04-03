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
public class TlscSseReportToSseReportConverter
    extends AutoRegisteredConverter<SSEReport, SseReport> {

    @Override
    public SseReport convert(final SSEReport src) {

        final Site site = convert(src.getSite(), Site.class);
        final SseFields sseFields = convert(src.getSSEFields(), SseFields.class);
        final ExtraFields extraFields = convert(src.getExtraFields(), ExtraFields.class);

        return new SseReport(site, sseFields, extraFields, src.getAdditionalProperties());
    }
}
