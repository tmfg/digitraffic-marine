package fi.livi.digitraffic.meri.dao.sse;

import java.time.ZonedDateTime;
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


    List<SseReport> findByLatestIsTrueOrderBySiteNumber();

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    List<SseReport> findByLastUpdateBetweenOrderBySiteNumberAscLastUpdateAsc(final ZonedDateTime from,
                                                                             final ZonedDateTime to,
                                                                             final Pageable page);

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    List<SseReport> findByLastUpdateBetweenAndSiteNumberOrderBySiteNumberAscLastUpdateAsc(final ZonedDateTime from,
                                                                                          final ZonedDateTime to,
                                                                                          final Integer siteNumber,
                                                                                          final Pageable page);

    SseReport findByLatestIsTrueAndSiteNumber(int siteNumber);

    @Modifying
    @Query(value = "UPDATE SSE_REPORT SET latest = FALSE WHERE site_number = :siteNumber AND latest = TRUE", nativeQuery = true)
    int markSiteLatestReportAsNotLatest(@Param(value = "siteNumber") final Integer siteNumber);
}
