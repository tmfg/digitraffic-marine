package fi.livi.digitraffic.meri.service.sse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;

@ConditionalOnNotWebApplication
@Service
public class SseUpdateService {

    private static final Logger log = LoggerFactory.getLogger(SseUpdateService.class);

    private final SseTlscReportRepository sseTlscReportRepository;
    private final SseReportRepository sseReportRepository;
    private final SseDataListener sseDataListener;
    private final SseConversionService sseConversionService;

    public SseUpdateService(final SseTlscReportRepository sseTlscReportRepository,
                            final SseReportRepository sseReportRepository,
                            final SseDataListener sseDataListener,
                            final SseConversionService sseConversionService) {
        this.sseTlscReportRepository = sseTlscReportRepository;
        this.sseReportRepository = sseReportRepository;
        this.sseDataListener = sseDataListener;
        this.sseConversionService = sseConversionService;
    }

    /**
     * This is called by daemon process and sseDataListener bean is awailable only for it.
     * @param maxCountToHandle
     * @return
     */
    @Transactional
    public int handleUnhandledSseReports(final Integer maxCountToHandle) {
        final List<SseTlscReport> unhandledReports = sseTlscReportRepository.findUnhandeldOldestFirst(maxCountToHandle);
        int success = 0;
        int failed = 0;
        for (SseTlscReport report : unhandledReports) {
            try {
                final SseReport saved = saveUnhandledSseReportToDomainAndMarkAsHandled(report);
                final SseFeature feature = sseConversionService.createSseFeatureFrom(saved);
                sseDataListener.receiveMessage(feature);
                success++;
            } catch (Exception e) {
                log.error(String.format("Error while handling SseTlscReport: %s", report.toString()), e);
                failed++;
            }
        }
        if (!unhandledReports.isEmpty()) {
            log.info("method=handleUnhandledSseReports handledCount={} failedCount={}", success, failed);
        }
        return unhandledReports.size();
    }

    private SseReport saveUnhandledSseReportToDomainAndMarkAsHandled(final SseTlscReport report) {

        final fi.livi.digitraffic.meri.model.sse.tlsc.SseReport toHandle = report.getReport();
        final SseSite site = toHandle.getSseSite();
        final SseFields sseFields = toHandle.getSseFields();
        final SseExtraFields extraFields = toHandle.getSseExtraFields();

        final SseReport toSave =
            new SseReport(
                ZonedDateTime.now(),
                true,
                site.getSiteNumber(),
                site.getSiteName(),
                site.getSiteType(),
                sseFields.getLastUpdate(),
                sseFields.getSeaState(),
                sseFields.getTrend(),
                sseFields.getWindWaveDir(),
                sseFields.getConfidence(),
                BigDecimal.valueOf(extraFields.getHeelAngle()),
                extraFields.getLightStatus(),
                extraFields.getTemperature(),
                BigDecimal.valueOf(extraFields.getCoordLongitude()),
                BigDecimal.valueOf(extraFields.getCoordLatitude()));

        sseReportRepository.markSiteLatestReportAsNotLatest(toSave.getSiteNumber());
        sseReportRepository.save(toSave);
        sseTlscReportRepository.markHandled(report.getId());
        return toSave;
    }
}
