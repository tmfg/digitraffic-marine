package fi.livi.digitraffic.meri.controller.sse;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.sse.SseServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static fi.livi.digitraffic.meri.controller.ApiConstants.*;
import static fi.livi.digitraffic.meri.model.Constants.*;

@Tag(name = SSE_V1_TAG, description = "SSE Controller")
@RestController
@Validated
@ConditionalOnWebApplication
public class SseControllerV1 {
    public static final String API_SSE_V1 = API_SSE + V1;

    private final SseServiceV1 sseServiceV1;

    public SseControllerV1(final SseServiceV1 sseServiceV1) {
        this.sseServiceV1 = sseServiceV1;
    }

    @Operation(summary = "Return SSE (Sea State Estimation) data as GeoJSON.  If from/to parameters are given, returs " +
        "measurements from history, otherwise returns the latest measurements")
    @GetMapping(path = API_SSE_V1 + "/measurements" , produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
        MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
        MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection measurements(
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
                return sseServiceV1.findMeasurements(siteNumber);
            }

            return sseServiceV1.findHistory(siteNumber, from, to);
        }
}
