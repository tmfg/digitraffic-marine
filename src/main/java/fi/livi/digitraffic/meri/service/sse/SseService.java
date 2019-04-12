package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;
import fi.livi.digitraffic.meri.util.StringUtil;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;
    private final SseTlscReportRepository sseTlscReportRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public SseService(final ConversionService conversionService,
                      final ObjectMapper objectMapper,
                      final SseTlscReportRepository sseTlscReportRepository,
                      final UpdatedTimestampRepository updatedTimestampRepository) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
        this.sseTlscReportRepository = sseTlscReportRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public int saveTlscSseReports(TlscSseReports tlscSseReports) {
        int count = 0;
        for (final SSEReport report : tlscSseReports.getSSEReports()) {
            final SseReport result = conversionService.convert(report, SseReport.class);
            SseTlscReport saved = sseTlscReportRepository.save(new SseTlscReport(result));
            count++;
            log.info("method=saveTlscSseReports report=\n{}", StringUtil.toJsonString(saved));
        }
        updatedTimestampRepository.setUpdated(SSE_DATA, ZonedDateTime.now(), getClass().getSimpleName());
        log.info("method=saveTlscSseReports countSaved={}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<SseTlscReport> findAllRaw() {
        return sseTlscReportRepository.findAll();
    }

    public SseFeatureCollection findAll() {
        final List<SseFeature> features = findAllRaw().stream().map(r -> {
            final SseSite site = r.getReport().getSseSite();
            final SseFields sseFields = r.getReport().getSseFields();
            final SseExtraFields extraFields = r.getReport().getSseExtraFields();
            final SseProperties sseProperties = new SseProperties(
                site.getSiteName(),
                sseFields.getLastUpdate(),
                SseProperties.SeaState.fromValue(sseFields.getSeaState()),
                SseProperties.Trend.fromValue(sseFields.getTrend()),
                sseFields.getWindWaveDir(),
                SseProperties.Confidence.fromValue(sseFields.getConfidence()),
                extraFields.getHeelAngle(),
                SseProperties.LightStatus.fromValue(extraFields.getLightStatus()),
                extraFields.getTemperature());

            sseProperties.addAdditionalProperties(site.getAdditionalProperties());
            sseProperties.addAdditionalProperties(sseFields.getAdditionalProperties());
            sseProperties.addAdditionalProperties(extraFields.getAdditionalProperties());

            try {
                if (extraFields.getCoordLongitude() == null || extraFields.getCoordLatitude() == null) {
                    return new SseFeature(new Point(), sseProperties, site.getSiteNumber());
                }
                return new SseFeature(new Point( extraFields.getCoordLongitude(), extraFields.getCoordLatitude()), sseProperties, site.getSiteNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        final ZonedDateTime updated = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        return new SseFeatureCollection(updated, features);
    }
}
