package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseSite;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;

@ConditionalOnWebApplication
@Component
public class TlscSseReportToSseReportConverter
    extends AutoRegisteredConverter<SSEReport, SseReport> {

    @Override
    public SseReport convert(final SSEReport src) {

        final SseSite sseSite = convert(src.getSite(), SseSite.class);
        final SseFields sseFields = convert(src.getSSEFields(), SseFields.class);
        final SseExtraFields sseExtraFields = convert(src.getExtraFields(), SseExtraFields.class);

        return new SseReport(sseSite, sseFields, sseExtraFields, src.getAdditionalProperties());
    }
}
