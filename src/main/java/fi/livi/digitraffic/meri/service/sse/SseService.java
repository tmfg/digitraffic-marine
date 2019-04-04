package fi.livi.digitraffic.meri.service.sse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.dao.sse.SseReportContainerRepository;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReportContainer;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;
    private final SseReportContainerRepository sseReportContainerRepository;

    public SseService(@Qualifier("conversionService")
                      final ConversionService conversionService,
                      final ObjectMapper objectMapper,
                      final SseReportContainerRepository sseReportContainerRepository) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
        this.sseReportContainerRepository = sseReportContainerRepository;
    }

    @Transactional
    public void saveTlscSseReports(TlscSseReports tlscSseReports) {
        tlscSseReports.getSSEReports().stream().forEach(r -> {
            final SseReport result = conversionService.convert(r, SseReport.class);
            try {
                SseReportContainer saved = sseReportContainerRepository.save(new SseReportContainer(result));
                log.info("method=saveTlscSseReports id={} report=\n{}", saved.getId(), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Transactional(readOnly = true)
    public List<SseReportContainer> findAll() {
        return sseReportContainerRepository.findAll();
    }
}
