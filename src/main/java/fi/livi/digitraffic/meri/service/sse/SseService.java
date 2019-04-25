package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;
import fi.livi.digitraffic.meri.util.StringUtil;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final SseTlscReportRepository sseTlscReportRepository;
    private final SseReportRepository sseReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public SseService(final ConversionService conversionService,
                      final SseTlscReportRepository sseTlscReportRepository,
                      final SseReportRepository sseReportRepository,
                      final UpdatedTimestampRepository updatedTimestampRepository) {
        this.conversionService = conversionService;
        this.sseTlscReportRepository = sseTlscReportRepository;
        this.sseReportRepository = sseReportRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public int saveTlscSseReports(TlscSseReports tlscSseReports) {
        int count = 0;
        for (final SSEReport report : tlscSseReports.getSSEReports()) {
            final SseReport result = conversionService.convert(report, SseReport.class);
            SseTlscReport saved = sseTlscReportRepository.save(new SseTlscReport(result));
            count++;
            log.info("method=saveTlscSseReports report=\n{}", StringUtil.toJsonString(saved));
        }
        updatedTimestampRepository.setUpdated(SSE_DATA, ZonedDateTime.now(), SseService.class.getSimpleName());
        log.info("method=saveTlscSseReports countSaved={}", count);
        return count;
    }

    @Transactional
    public int handleUnhandledSseReports(final Integer maxCountToHandle) {
        final List<SseTlscReport> unhandledReports = sseTlscReportRepository.findUnhandeldOldestFirst(maxCountToHandle);
        unhandledReports.forEach(report -> saveUnhandledSseReportToDomainAndMarkAsHandled(report));
        if (!unhandledReports.isEmpty()) {
            log.info("method=handleUnhandledSseReports handledCount={}", unhandledReports.size());
        }
        return unhandledReports.size();
    }

    private void saveUnhandledSseReportToDomainAndMarkAsHandled(final SseTlscReport report) {

        final SseReport toHandle = report.getReport();
        final SseSite site = toHandle.getSseSite();
        final SseFields sseFields = toHandle.getSseFields();
        final SseExtraFields extraFields = toHandle.getSseExtraFields();

        final fi.livi.digitraffic.meri.domain.sse.SseReport toSave =
            new fi.livi.digitraffic.meri.domain.sse.SseReport(
                ZonedDateTime.now(),
                true,
                site.getSiteNumber(),
                site.getSiteName(),
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
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findLatest() {
        final List<fi.livi.digitraffic.meri.domain.sse.SseReport> latest = sseReportRepository.findByLatestIsTrueOrderBySiteNumber();

        final List<SseFeature> features = sseReportRepository.findByLatestIsTrueOrderBySiteNumber().stream().map(r -> {

            final SseProperties sseProperties = new SseProperties(
                r.getSiteName(),
                r.getLastUpdate(),
                r.getSeaState(),
                r.getTrend(),
                r.getWindWaveDir(),
                r.getConfidence(),
                r.getHeelAngle(),
                r.getLightStatus(),
                r.getTemperature());

            try {
                if (r.getLongitude() == null || r.getLatitude() == null) {
                    return new SseFeature(new Point(), sseProperties, r.getSiteNumber());
                }
                return new SseFeature(new Point(r.getLongitude().doubleValue(), r.getLatitude().doubleValue()),
                                      sseProperties, r.getSiteNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        final ZonedDateTime updated = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        return new SseFeatureCollection(updated, features);
    }
}
