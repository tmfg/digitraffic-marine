package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.util.StringUtil;

@ConditionalOnWebApplication
@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final SseTlscReportRepository sseTlscReportRepository;
    private final SseReportRepository sseReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final SseConversionService sseConversionService;

    private static final ZonedDateTime MIN_ZONED_DATE_TIME = Instant.ofEpochMilli(0).atZone(ZoneOffset.UTC);
    // 1.1.2942
    private static final ZonedDateTime MAX_ZONED_DATE_TIME = Instant.ofEpochMilli(30673382400000L).atZone(ZoneOffset.UTC);

    private static final int MAX_QUERY_RESULT_SIZE = 1000;

    public SseService(final ConversionService conversionService,
                      final SseTlscReportRepository sseTlscReportRepository,
                      final SseReportRepository sseReportRepository,
                      final UpdatedTimestampRepository updatedTimestampRepository,
                      final SseConversionService sseConversionService) {
        this.conversionService = conversionService;
        this.sseTlscReportRepository = sseTlscReportRepository;
        this.sseReportRepository = sseReportRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.sseConversionService = sseConversionService;
    }

    @Transactional
    public int saveTlscSseReports(TlscSseReports tlscSseReports) {
        int count = 0;
        for (final fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport report : tlscSseReports.getSSEReports()) {
            final fi.livi.digitraffic.meri.model.sse.tlsc.SseReport result =
                conversionService.convert(report, fi.livi.digitraffic.meri.model.sse.tlsc.SseReport.class);
            SseTlscReport saved = sseTlscReportRepository.save(new SseTlscReport(result));
            count++;
            log.info("method=saveTlscSseReports report=\n{}", StringUtil.toJsonString(saved));
        }
        if (count > 0) {
            updatedTimestampRepository.setUpdated(SSE_DATA, ZonedDateTime.now(), SseService.class.getSimpleName());
        }
        log.info("method=saveTlscSseReports countSaved={}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findLatest() {
        return sseConversionService.createSseFeatureCollectionFrom(sseReportRepository.findByLatestIsTrueOrderBySiteNumber());
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findLatest(final int siteNumber) {
        checSiteNumberParameter(siteNumber);
        SseReport report = sseReportRepository.findByLatestIsTrueAndSiteNumber(siteNumber);
        return sseConversionService.createSseFeatureCollectionFrom(Collections.singletonList(report));
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findHistory(final ZonedDateTime from, final ZonedDateTime to) throws BadRequestException {
        checkFromToParameters(from, to);
        final List<SseReport> history =
            sseReportRepository.findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(from != null ? from : MIN_ZONED_DATE_TIME,
                                                                                         to != null ? to : MAX_ZONED_DATE_TIME,
                                                                                         PageRequest.of(0, MAX_QUERY_RESULT_SIZE+1));

        checkMaxResultSize(history);

        return sseConversionService.createSseFeatureCollectionFrom(history);
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findHistory(final int siteNumber, final ZonedDateTime from, final ZonedDateTime to) throws BadRequestException {
        checSiteNumberParameter(siteNumber);
        checkFromToParameters(from, to);
        final List<SseReport> history =
            sseReportRepository.findByLastUpdateBetweenAndSiteNumberOrderBySiteNumberAscLastUpdateAsc(from != null ? from : MIN_ZONED_DATE_TIME,
                                                                                                      to != null ? to : MAX_ZONED_DATE_TIME,
                                                                                                      siteNumber,
                                                                                                      PageRequest.of(0, MAX_QUERY_RESULT_SIZE+1));

        checkMaxResultSize(history);

        return sseConversionService.createSseFeatureCollectionFrom(history);
    }

    private void checSiteNumberParameter(final int siteNumber) {
        if (!sseReportRepository.existsBySiteNumber(siteNumber)) {
            throw new IllegalArgumentException(String.format("No SSE data exists with siteNumber %d", siteNumber));
        }
    }

    private void checkFromToParameters(ZonedDateTime from, ZonedDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Value of parameter from should be before value of parameter to");
        }
    }

    private void checkMaxResultSize(final List<SseReport> history) {
        if (history.size() > MAX_QUERY_RESULT_SIZE) {
            throw new BadRequestException("The search result is too big (over 1000 items), try to narrow down your search criteria.");
        }
    }
}
