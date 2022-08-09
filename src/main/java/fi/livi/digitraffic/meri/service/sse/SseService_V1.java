package fi.livi.digitraffic.meri.service.sse;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.util.TimeUtil;

@Service
public class SseService_V1 {

    private static final Logger log = LoggerFactory.getLogger(SseService_V1.class);

    private final SseReportRepository sseReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final SseConversionService sseConversionService;

    private static final Instant MIN_ZONED_DATE_TIME = Instant.ofEpochMilli(0);
    // 1.1.2942
    private static final Instant MAX_ZONED_DATE_TIME = Instant.ofEpochMilli(30673382400000L);

    private static final int MAX_QUERY_RESULT_SIZE = 1000;

    public SseService_V1(final SseReportRepository sseReportRepository,
                         final UpdatedTimestampRepository updatedTimestampRepository,
                         final SseConversionService sseConversionService) {
        this.sseReportRepository = sseReportRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.sseConversionService = sseConversionService;
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
    public SseFeatureCollection findHistory(final Instant from, final Instant to) throws BadRequestException {
        checkFromToParameters(from, to);
        final List<SseReport> history =
            sseReportRepository.findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(from != null ? from : MIN_ZONED_DATE_TIME,
                                                                                         to != null ? to : MAX_ZONED_DATE_TIME,
                                                                                         PageRequest.of(0, MAX_QUERY_RESULT_SIZE+1));

        checkMaxResultSize(history);

        return sseConversionService.createSseFeatureCollectionFrom(history);
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findHistory(final int siteNumber, final Instant from, final Instant to) throws BadRequestException {
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

    @Transactional(readOnly = true)
    public SseFeatureCollection findCreatedAfter(final Instant after) throws BadRequestException {
        final List<SseReport> history =
            sseReportRepository.findByCreatedAfterOrderByCreatedAsc(TimeUtil.withoutMillis(after));
        return sseConversionService.createSseFeatureCollectionFrom(history);
    }

    private void checSiteNumberParameter(final int siteNumber) {
        if (!sseReportRepository.existsBySiteNumber(siteNumber)) {
            throw new IllegalArgumentException(String.format("No SSE data exists with siteNumber %d", siteNumber));
        }
    }

    private void checkFromToParameters(Instant from, Instant to) {
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
