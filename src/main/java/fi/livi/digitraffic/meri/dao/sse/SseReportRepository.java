package fi.livi.digitraffic.meri.dao.sse;

import java.time.Instant;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.sse.SseReport;

@Repository
public interface SseReportRepository extends PagingAndSortingRepository<SseReport, Long> {


    boolean existsBySiteNumber(final int siteNumber);

    List<SseReport> findByLatestIsTrueOrderBySiteNumber();

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    List<SseReport> findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(final Instant from,
                                                                             final Instant to,
                                                                             final Pageable page);

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    List<SseReport> findByLastUpdateBetweenAndSiteNumberOrderBySiteNumberAscLastUpdateAsc(final Instant from,
                                                                                          final Instant to,
                                                                                          final Integer siteNumber,
                                                                                          final Pageable page);

    SseReport findByLatestIsTrueAndSiteNumber(final int siteNumber);

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    List<SseReport> findByCreatedAfterOrderByCreatedAsc(final Instant created);

    @Modifying
    @Query(value = "UPDATE SSE_REPORT SET latest = FALSE WHERE site_number = :siteNumber AND latest = TRUE", nativeQuery = true)
    int markSiteLatestReportAsNotLatest(@Param(value = "siteNumber") final Integer siteNumber);
}
