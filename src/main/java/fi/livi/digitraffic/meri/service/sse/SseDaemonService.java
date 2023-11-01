package fi.livi.digitraffic.meri.service.sse;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.common.util.TimeUtil;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.model.sse.SseReport;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.sse.v1.SseConversionServiceV1;

@ConditionalOnNotWebApplication
@Service
public class SseDaemonService {

    private final SseReportRepository sseReportRepository;
    private final SseConversionServiceV1 sseConversionServiceV1;

    public SseDaemonService(final SseReportRepository sseReportRepository,
                            final SseConversionServiceV1 sseConversionServiceV1) {
        this.sseReportRepository = sseReportRepository;
        this.sseConversionServiceV1 = sseConversionServiceV1;
    }

    @Transactional(readOnly = true)
    public SseFeatureCollectionV1 findCreatedAfter(final Instant after) throws BadRequestException {
        final List<SseReport> history =
            sseReportRepository.findByCreatedAfterOrderByCreatedAsc(TimeUtil.withoutMillis(after));
        return sseConversionServiceV1.createSseFeatureCollectionFrom(history);
    }
}
