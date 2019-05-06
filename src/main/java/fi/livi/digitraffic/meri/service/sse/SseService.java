package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
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
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.util.StringUtil;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final SseTlscReportRepository sseTlscReportRepository;
    private final SseReportRepository sseReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private static final ZonedDateTime MIN_ZONED_DATE_TIME = Instant.ofEpochMilli(0).atZone(ZoneOffset.UTC);
    // 1.1.2942
    private static final ZonedDateTime MAX_ZONED_DATE_TIME = Instant.ofEpochMilli(30673382400000L).atZone(ZoneOffset.UTC);

    private static final int MAX_QUERY_RESULT_SIZE = 1000;

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
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findLatest() {

        return createSseFeatureCollectionFrom(sseReportRepository.findByLatestIsTrueOrderBySiteNumber());
    }

    public SseFeatureCollection findLatest(final int siteNumber) {
        fi.livi.digitraffic.meri.domain.sse.SseReport report = sseReportRepository.findByLatestIsTrueAndSiteNumber(siteNumber);
        if (report == null) {
            throw new ObjectNotFoundException("Sse site", siteNumber);
        }
        return createSseFeatureCollectionFrom(Collections.singletonList(report));
    }

    public SseFeatureCollection findHistory(final ZonedDateTime from, final ZonedDateTime to) {
        final List<fi.livi.digitraffic.meri.domain.sse.SseReport> history =
            sseReportRepository.findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(from != null ? from : MIN_ZONED_DATE_TIME,
                                                                                         to != null ? to : MAX_ZONED_DATE_TIME,
                                                                                         PageRequest.of(0, MAX_QUERY_RESULT_SIZE+1));

        checkMaxResultSize(history);

        return createSseFeatureCollectionFrom(history);
    }

    public SseFeatureCollection findHistory(final int siteNumber, final ZonedDateTime from, final ZonedDateTime to) {
        final List<fi.livi.digitraffic.meri.domain.sse.SseReport> history =
            sseReportRepository.findByLastUpdateBetweenAndSiteNumberOrderBySiteNumberAscLastUpdateAsc(from != null ? from : MIN_ZONED_DATE_TIME,
                                                                                                      to != null ? to : MAX_ZONED_DATE_TIME,
                                                                                                      siteNumber,
                                                                                                      PageRequest.of(0, MAX_QUERY_RESULT_SIZE+1));

        checkMaxResultSize(history);

        return createSseFeatureCollectionFrom(history);
    }

    private void checkMaxResultSize(final List<fi.livi.digitraffic.meri.domain.sse.SseReport> history) {
        if (history.size() > MAX_QUERY_RESULT_SIZE) {
            throw new BadRequestException("The search result is too big (over 1000 items), try to narrow down your search criteria.");
        }
    }

    private SseFeatureCollection createSseFeatureCollectionFrom(List<fi.livi.digitraffic.meri.domain.sse.SseReport> sseReports) {
        final ZonedDateTime updated = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        final List<SseFeature> features = sseReports.stream().map(r -> createSseFeatureFrom(r)).collect(Collectors.toList());

        return new SseFeatureCollection(updated, features);
    }

    private SseFeature createSseFeatureFrom(final fi.livi.digitraffic.meri.domain.sse.SseReport sseReport) {
        final SseProperties sseProperties = new SseProperties(
            sseReport.getSiteName(),
            sseReport.getSiteType(),
            sseReport.getLastUpdate().withZoneSameInstant(ZoneOffset.UTC),
            sseReport.getSeaState(),
            sseReport.getTrend(),
            sseReport.getWindWaveDir(),
            sseReport.getConfidence(),
            sseReport.getHeelAngle(),
            sseReport.getLightStatus(),
            sseReport.getTemperature());

        if (sseReport.getLongitude() == null || sseReport.getLatitude() == null) {
            return new SseFeature(new Point(), sseProperties, sseReport.getSiteNumber());
        }
        return new SseFeature(new Point(sseReport.getLongitude().doubleValue(), sseReport.getLatitude().doubleValue()),
                              sseProperties, sseReport.getSiteNumber());
    }
}
