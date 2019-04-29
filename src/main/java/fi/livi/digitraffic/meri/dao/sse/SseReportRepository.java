package fi.livi.digitraffic.meri.dao.sse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.sse.SseReport;

@Repository
public interface SseReportRepository extends JpaRepository<SseReport, Long> {


    List<SseReport> findByLatestIsTrueOrderBySiteNumber();

    SseReport findByLatestIsTrueAndSiteNumber(int siteNumber);

    @Modifying
    @Query(value = "UPDATE SSE_REPORT SET latest = FALSE WHERE site_number = :siteNumber AND latest = TRUE", nativeQuery = true)
    int markSiteLatestReportAsNotLatest(@Param(value = "siteNumber") final Integer siteNumber);
}