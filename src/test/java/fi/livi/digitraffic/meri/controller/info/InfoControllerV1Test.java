package fi.livi.digitraffic.meri.controller.info;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.model.DataSource;
import fi.livi.digitraffic.meri.service.DataStatusService;

public class InfoControllerV1Test extends AbstractWebTestBase {

    @Autowired
    private DataStatusService dataStatusService;

    @Test
    public void updateTimes() throws Exception {

        final String sseInterval = dataStatusService.getDataSourceUpdateInterval(DataSource.SSE_DATA).toString();
        final String portCallInterval = dataStatusService.getDataSourceUpdateInterval(DataSource.PORT_CALL).toString();

        final ResultActions response =
            logDebugResponse(
                executeGet(InfoControllerV1.API_INFO_V1 + InfoControllerV1.UPDATE_TIMES));
        expectOk(response)
            .andExpect(jsonPath("updateTimes[*].api").exists())
            .andExpect(jsonPath("updateTimes[*].subtype").exists())
            .andExpect(jsonPath("updateTimes[*].dataUpdatedTime").exists())
            .andExpect(jsonPath("updateTimes[*].dataCheckedTime").exists())
            .andExpect(jsonPath("updateTimes[*].dataUpdateInterval").exists())
            .andExpect(jsonPath("$.updateTimes[?(@.api=='/api/sse/v1/measurements')].dataUpdateInterval").value(sseInterval))
            .andExpect(jsonPath("$.updateTimes[?(@.api=='/api/port-call/v1/port-calls')].dataUpdateInterval", Matchers.hasItem(portCallInterval)))
            ;
    }
}
