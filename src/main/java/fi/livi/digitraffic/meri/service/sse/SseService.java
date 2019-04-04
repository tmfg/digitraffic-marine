package fi.livi.digitraffic.meri.service.sse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.dao.sse.SseReportContainerRepository;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReportContainer;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;
    private final SseReportContainerRepository sseReportContainerRepository;

    @Autowired
    public SseService(@Qualifier("conversionService")
                      final ConversionService conversionService,
                      final ObjectMapper objectMapper,
                      final SseReportContainerRepository sseReportContainerRepository) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
        this.sseReportContainerRepository = sseReportContainerRepository;
    }

    @Transactional
    public int saveTlscSseReports(TlscSseReports tlscSseReports) {
        int count = 0;
        for (final SSEReport report : tlscSseReports.getSSEReports()) {
            final SseReport result = conversionService.convert(report, SseReport.class);
            try {
                SseReportContainer saved = sseReportContainerRepository.save(new SseReportContainer(result));
                count++;
                log.info("method=saveTlscSseReports id={} report=\n{}", saved.getId(), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved));
            } catch (JsonProcessingException e) {
                log.warn("Json print failed", e);
            }
        }
        log.info("method=saveTlscSseReports countSaved={}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<SseReportContainer> findAll() {
        return sseReportContainerRepository.findAll();
    }
}
