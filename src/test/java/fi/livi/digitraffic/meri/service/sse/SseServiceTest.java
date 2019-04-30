package fi.livi.digitraffic.meri.service.sse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.ConversionService;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.sse.SseTlscReportRepository;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseTlscReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.ExtraFields;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEFields;
import fi.livi.digitraffic.meri.external.tlsc.sse.SSEReport;
import fi.livi.digitraffic.meri.external.tlsc.sse.Site;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseReport;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseSite;

@Transactional
public class SseServiceTest extends AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(MessageConverter.class);

    @SpyBean
    private SseService sseService;

    @SpyBean
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private SseTlscReportRepository sseTlscReportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveTlscSseReports() throws Exception {

        final String postJson = readFile("sse/example-sse-report.json");

        final TlscSseReports postObject = objectMapper.readerFor(TlscSseReports.class).readValue(postJson);

        verifyConverterTimes(0, SSEReport.class,   SseReport.class);
        verifyConverterTimes(0, Site.class,        SseSite.class);
        verifyConverterTimes(0, SSEFields.class,   SseFields.class);
        verifyConverterTimes(0, ExtraFields.class, SseExtraFields.class);

        Mockito.verify(sseTlscReportRepository, Mockito.times(0))
            .save(any(SseTlscReport.class));

        Mockito.verify(conversionService, Mockito.times(0))
            .convert(any(Site.class), eq(SseSite.class));

        Mockito.verify(conversionService, Mockito.times(0))
            .convert(any(SSEFields.class), eq(SseFields.class));

        Mockito.verify(conversionService, Mockito.times(0))
            .convert(any(ExtraFields.class), eq(SseExtraFields.class));

        final int saved = sseService.saveTlscSseReports(postObject);

        verifyConverterTimes(2, SSEReport.class,   SseReport.class);
        verifyConverterTimes(2, Site.class,        SseSite.class);
        verifyConverterTimes(2, SSEFields.class,   SseFields.class);
        verifyConverterTimes(2, ExtraFields.class, SseExtraFields.class);

        Mockito.verify(sseTlscReportRepository, Mockito.times(2))
            .save(any(SseTlscReport.class));

        Assert.assertEquals(2, saved);
    }

    private void verifyConverterTimes(final int times, Class<?> sourceType, Class<?> targetType) {
        Mockito.verify(conversionService, Mockito.times(times))
            .convert(any(sourceType), eq(targetType));
    }
}
