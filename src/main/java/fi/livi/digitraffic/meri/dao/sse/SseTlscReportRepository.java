package fi.livi.digitraffic.meri.dao.sse;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;

@Repository
public interface SseTlscReportRepository extends JpaRepository<SseTlscReport, Long> {

    List<SseTlscReport> findByHandledIsNullOrderByIdAsc(final Pageable pageable);
    List<SseTlscReport> findByHandledIsNullOrderByIdAsc();

    default List<SseTlscReport> findUnhandeldOldestFirst(final Integer maxResultCount) {
        if (maxResultCount != null && maxResultCount > 0) {
            return findByHandledIsNullOrderByIdAsc(PageRequest.of(0, maxResultCount));
        }
        return findByHandledIsNullOrderByIdAsc();
    }


    @Modifying
    @Query(value = "UPDATE SSE_TLSC_REPORT SET handled = clock_timestamp() WHERE ID = :id", nativeQuery = true)
    int markHandled(@Param(value = "id") final Long id);
}
