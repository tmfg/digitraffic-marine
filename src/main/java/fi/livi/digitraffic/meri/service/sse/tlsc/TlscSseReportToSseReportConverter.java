package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;

@ConditionalOnWebApplication
@Component
public class TlscSseReportToSseReportConverter
    extends AutoRegisteredConverter<SSEReport, SseReport> {

    private static final Logger log = LoggerFactory.getLogger(TlscSseReportToSseReportConverter.class);

    @Override
    public SseReport convert(final SSEReport src) {

        final SseSite sseSite = convert(src.getSite(), SseSite.class);
        final SseFields sseFields = convert(src.getSSEFields(), SseFields.class);
        final SseExtraFields sseExtraFields = convert(src.getExtraFields(), SseExtraFields.class);
        if (!src.getAdditionalProperties().isEmpty()) {
            log.warn("No mapings at {} for additionalProperties={}", src.getClass(), src.getAdditionalProperties());
        }
        return new SseReport(sseSite, sseFields, sseExtraFields, src.getAdditionalProperties());
    }
}
