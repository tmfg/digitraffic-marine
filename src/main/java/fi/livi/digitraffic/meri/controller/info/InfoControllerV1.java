package fi.livi.digitraffic.meri.controller.info;

import static fi.livi.digitraffic.meri.controller.ApiConstants.API_INFO;
import static fi.livi.digitraffic.meri.controller.ApiConstants.BETA;
import static fi.livi.digitraffic.meri.controller.ApiConstants.INFO_BETA_TAG;
import static fi.livi.digitraffic.meri.controller.ApiConstants.V1;
import static fi.livi.digitraffic.meri.controller.DtMediaType.APPLICATION_JSON_VALUE;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.dto.info.v1.UpdateInfosDtoV1;
import fi.livi.digitraffic.meri.service.DataStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = INFO_BETA_TAG, description = "Info APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class InfoControllerV1 {

    public static final String API_INFO_BETA = API_INFO + BETA;
    public static final String API_INFO_V1 = API_INFO + V1;

    public static final String UPDATE_TIMES = "/update-times";

    private final DataStatusService dataStatusService;

    public InfoControllerV1(final DataStatusService dataStatusService) {
        this.dataStatusService = dataStatusService;
    }

    /* METADATA */

    @Operation(summary = "Infos about apis data update times")
    @RequestMapping(method = RequestMethod.GET,
                    path = API_INFO_V1 + UPDATE_TIMES,
                    produces = { APPLICATION_JSON_VALUE })
    @ApiResponses(@ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of weather Station Feature Collections"))
    public UpdateInfosDtoV1 dataUpdatedInfos() {
        return dataStatusService.getUpdatedInfos();
    }
}

