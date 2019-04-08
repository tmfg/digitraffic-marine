package fi.livi.digitraffic.meri.dao.sse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;

@Repository
public interface SseTlscReportRepository extends JpaRepository<SseTlscReport, Long> {



}
