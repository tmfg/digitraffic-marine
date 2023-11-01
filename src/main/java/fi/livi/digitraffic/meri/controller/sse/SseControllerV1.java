package fi.livi.digitraffic.meri.controller.sse;

import static fi.livi.digitraffic.meri.controller.ApiConstants.API_SSE;
import static fi.livi.digitraffic.meri.controller.ApiConstants.SSE_V1_TAG;
import static fi.livi.digitraffic.meri.controller.ApiConstants.V1;
import static fi.livi.digitraffic.meri.controller.Constants.ISO_DATE_TIME_FROM_DOC;
import static fi.livi.digitraffic.meri.controller.Constants.ISO_DATE_TIME_FROM_VALUE;
import static fi.livi.digitraffic.meri.controller.Constants.ISO_DATE_TIME_TO_DOC;
import static fi.livi.digitraffic.meri.controller.Constants.ISO_DATE_TIME_TO_VALUE;

import java.time.Instant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.service.sse.v1.SseWebServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = SSE_V1_TAG, description = "Sea State Estimate (SSE) APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class SseControllerV1 {
    public static final String API_SSE_V1 = API_SSE + V1;
    public static final String MEASUREMENTS = "/measurements";

    private final SseWebServiceV1 sseWebServiceV1;

    public SseControllerV1(final SseWebServiceV1 sseWebServiceV1) {
        this.sseWebServiceV1 = sseWebServiceV1;
    }

    @Operation(summary = "Return Sea State Estimation (SSE) data as GeoJSON.  If from/to parameters are given, returs " +
                         "measurements from history, otherwise returns the latest measurements")
    @GetMapping(path = API_SSE_V1 + MEASUREMENTS , produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
        MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
        MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollectionV1 measurements(
        @Parameter(description = "SSE site number")
        @RequestParam(value = "siteNumber", required = false)
        final Integer siteNumber,

        @Parameter(description = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from", required = false)
        final Instant from,

        @Parameter(description = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to", required = false)
        final Instant to) {
            if (from == null && to == null) {
                return sseWebServiceV1.findMeasurements(siteNumber);
            }

            return sseWebServiceV1.findHistory(siteNumber, from, to);
        }
}
