package fi.livi.digitraffic.meri.service.sse;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnWebApplication
public class SseServiceV1 {
    private final SseReportRepository sseReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final SseConversionService sseConversionService;

    private static final Instant MIN_ZONED_DATE_TIME = Instant.ofEpochMilli(0);
    // 1.1.2942
    private static final Instant MAX_ZONED_DATE_TIME = Instant.ofEpochMilli(30673382400000L);

    private static final int MAX_QUERY_RESULT_SIZE = 1000;


    public SseServiceV1(final SseReportRepository sseReportRepository,
                        final UpdatedTimestampRepository updatedTimestampRepository,
                        final SseConversionService sseConversionService) {
        this.sseReportRepository = sseReportRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.sseConversionService = sseConversionService;
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findMeasurements(final Integer siteNumber) {
        if (siteNumber != null) {
            final SseReport report = sseReportRepository.findByLatestIsTrueAndSiteNumber(siteNumber);
            return sseConversionService.createSseFeatureCollectionFrom(Collections.singletonList(report));
        }

        return sseConversionService.createSseFeatureCollectionFrom(sseReportRepository.findByLatestIsTrueOrderBySiteNumber());
    }

    @Transactional(readOnly = true)
    public SseFeatureCollection findHistory(final Integer siteNumber,
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

        return sseConversionService.createSseFeatureCollectionFrom(history);
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
