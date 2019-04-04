package fi.livi.digitraffic.meri.controller.sse;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_SSE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.domain.sse.tlsc.SseReportContainer;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.service.sse.SseService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_SSE_PATH)
@ConditionalOnWebApplication
public class SSEController {

    private static final Logger log = LoggerFactory.getLogger(SSEController.class);
    public static final String ADD_PATH = "/add";

    private final SseService sseService;
    private final ObjectMapper objectMapper;

    public SSEController(final SseService sseService,
                         final ObjectMapper objectMapper) {
        this.sseService = sseService;
        this.objectMapper = objectMapper;
    }

    @ApiOperation("Return list of all berths, port areas and locations.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SseReportContainer> listAllSseDatas() {
        return sseService.findAll();
    }


//    @ApiIgnore
    @ApiOperation("Saving Sea State Estimation data")
    @ApiResponses(@ApiResponse(code = 200, message = "Successful post of SSE data"))
    @PostMapping(path = ADD_PATH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
