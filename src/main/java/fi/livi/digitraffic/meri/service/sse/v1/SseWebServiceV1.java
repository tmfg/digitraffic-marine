package fi.livi.digitraffic.meri.service.sse.v1;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.model.sse.SseReport;
import fi.livi.digitraffic.meri.service.BadRequestException;

@Service
@ConditionalOnWebApplication
public class SseWebServiceV1 {
    private final SseReportRepository sseReportRepository;
    private final SseConversionServiceV1 sseConversionServiceV1;

    private static final Instant MIN_ZONED_DATE_TIME = Instant.ofEpochMilli(0);
    // 1.1.2942
    private static final Instant MAX_ZONED_DATE_TIME = Instant.ofEpochMilli(30673382400000L);
    private static final int MAX_QUERY_RESULT_SIZE = 1000;

    public SseWebServiceV1(final SseReportRepository sseReportRepository,
                           final SseConversionServiceV1 sseConversionServiceV1) {
        this.sseReportRepository = sseReportRepository;
        this.sseConversionServiceV1 = sseConversionServiceV1;
    }

    @Transactional(readOnly = true)
    public SseFeatureCollectionV1 findMeasurements(final Integer siteNumber) {
        final List<SseReport> reports;

        if (siteNumber != null) {
            final SseReport report = sseReportRepository.findByLatestIsTrueAndSiteNumber(siteNumber);

            reports = report == null ? Collections.emptyList() : Collections.singletonList(report);
        } else {
            reports = sseReportRepository.findByLatestIsTrueOrderBySiteNumber();
        }

        return sseConversionServiceV1.createSseFeatureCollectionFrom(reports);
    }

    @Transactional(readOnly = true)
    public SseFeatureCollectionV1 findHistory(final Integer siteNumber,
                                              final Instant from,
                                              final Instant to) {
        checkFromToParameters(from, to);

        final Instant checkedFrom = from != null ? from : MIN_ZONED_DATE_TIME;
        final Instant checkedTo = to != null ? to : MAX_ZONED_DATE_TIME;
        final PageRequest maxSize = PageRequest.of(0, MAX_QUERY_RESULT_SIZE + 1);
        final List<SseReport> history;

        if (siteNumber != null) {
            history = sseReportRepository.findByLastUpdateBetweenAndSiteNumberOrderBySiteNumberAscLastUpdateAsc(
                checkedFrom, checkedTo, siteNumber, maxSize);
        } else {
            history = sseReportRepository.findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(
                checkedFrom, checkedTo, maxSize);
        }
        checkMaxResultSize(history);

        return sseConversionServiceV1.createSseFeatureCollectionFrom(history);
    }

    private void checkMaxResultSize(final List<SseReport> history) {
        if (history.size() > MAX_QUERY_RESULT_SIZE) {
            throw new BadRequestException("The search result is too big (over 1000 items), try to narrow down your search criteria.");
        }
    }

    private static void checkFromToParameters(final Instant from, final Instant to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Value of parameter from should be before value of parameter to");
        }
    }
}
