package fi.livi.digitraffic.meri.service.sse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.util.StringUtil;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;
    private final SseTlscReportRepository sseTlscReportRepository;

    @Autowired
    public SseService(final ConversionService conversionService,
                      final ObjectMapper objectMapper,
                      final SseTlscReportRepository sseTlscReportRepository) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
        this.sseTlscReportRepository = sseTlscReportRepository;
    }

    @Transactional
    public int saveTlscSseReports(TlscSseReports tlscSseReports) {
        int count = 0;
        for (final SSEReport report : tlscSseReports.getSSEReports()) {
            final SseReport result = conversionService.convert(report, SseReport.class);
            SseTlscReport saved = sseTlscReportRepository.save(new SseTlscReport(result));
            count++;
            log.info("method=saveTlscSseReports id={} report=\n{}", saved.getId(), StringUtil.toJsonString(saved));
        }
        log.info("method=saveTlscSseReports countSaved={}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<SseTlscReport> findAll() {
        return sseTlscReportRepository.findAll();
    }
}
