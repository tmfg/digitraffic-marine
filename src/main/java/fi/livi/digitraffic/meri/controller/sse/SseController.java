package fi.livi.digitraffic.meri.controller.sse;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_SSE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_VALUE;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_VALUE;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.sse.SseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api
@RestController
@RequestMapping(API_V1_BASE_PATH + API_SSE_PATH)
@ConditionalOnWebApplication
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    public static final String CODE_400_SEARCH_RESULT_TOO_BIG = "The search result is too big (over 1000 items)";
    public static final String CODE_400_NOT_EXISTS_WITH_IDENTIFIER = "Objects not exists with given identifier";
    public static final String CODE_400_ILLEGAL_ARGUMENTS = "Illegal arguments";

    public static final String LATEST_PATH = "/latest";
    public static final String HISTORY_PATH = "/history";

    public static final String ADD_PATH = "/add";

    private final SseService sseService;
    private final ObjectMapper objectMapper;

    public SseController(final SseService sseService,
                         final ObjectMapper objectMapper) {
        this.sseService = sseService;
        this.objectMapper = objectMapper;
    }

    /* GET requests */

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON")
    @GetMapping(path = LATEST_PATH , produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findLatest() {
        return sseService.findLatest();
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON for given site")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_NOT_EXISTS_WITH_IDENTIFIER)
    })
    @GetMapping(path = LATEST_PATH + "/{siteNumber}", produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                                   MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                   MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findLatest(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable("siteNumber")
        final int siteNumber) {

        return sseService.findLatest(siteNumber);
    }

    @ApiOperation("Return SSE history data (Sea State Estimation) data as GeoJSON for given time")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_SEARCH_RESULT_TOO_BIG + " or " +
            CODE_400_ILLEGAL_ARGUMENTS)
    })
    @GetMapping(path = HISTORY_PATH, produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findHistory(

        @ApiParam(value = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE, required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from")
        final ZonedDateTime from,

        @ApiParam(value = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE, required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to")
        final ZonedDateTime to) {

        return sseService.findHistory(from, to);
    }

    @ApiOperation("Return SSE history data (Sea State Estimation) data as GeoJSON for given site and time")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_SEARCH_RESULT_TOO_BIG + " or " +
            CODE_400_NOT_EXISTS_WITH_IDENTIFIER + " or " +
            CODE_400_ILLEGAL_ARGUMENTS)
    })
    @GetMapping(path = HISTORY_PATH + "/{siteNumber}", produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                                    MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                    MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findHistoryBySite(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable(value = "siteNumber")
        final Integer siteNumber,

        @ApiParam(value = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from", required = false)
        final ZonedDateTime from,

        @ApiParam(value = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to", required = false)
        final ZonedDateTime to) {

        return sseService.findHistory(siteNumber, from, to);
    }


    /* POST requests */

    @ApiIgnore
    @PostMapping(path = ADD_PATH, consumes = MediaTypes.MEDIA_TYPE_APPLICATION_JSON, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public AddInfo addSseData(@RequestBody TlscSseReports tlscSseReports) throws JsonProcessingException {

        log.info("method=postSseData JSON=\n{}",
                 objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tlscSseReports));

        final int savedCount = sseService.saveTlscSseReports(tlscSseReports);
        return new AddInfo(savedCount);
    }


    @ApiModel(description = "Info for successful saving of SSE reports")
    public class AddInfo {

        @ApiModelProperty(value = "How many reports was saved", required = true)
        @JsonProperty(value = "count", required = true)
        private final int count;

        private AddInfo(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }
}
