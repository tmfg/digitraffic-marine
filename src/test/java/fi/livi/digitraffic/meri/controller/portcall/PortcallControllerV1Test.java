package fi.livi.digitraffic.meri.controller.portcall;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_METADATA;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;

@Transactional
public class PortcallControllerV1Test extends AbstractTestBase {

    private final static int PORT_LOCATIONS = 19243;
    private final static int FIHEL_PORTS = 8;
    private final static int OTHER_PORTS = 244;
    private final static int FIHEL_BERTHS = 101;
    private final static int OTHER_BERTHS = 16;
    private final static String UNKNOWN_LOCODE = "UNKNOWN_LOCODE";

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;
    private String dataUpdatedTime;

    @BeforeEach
    public void initData() {
        final Instant metadataUpdated = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        updatedTimestampRepository.setUpdated(PORT_METADATA, metadataUpdated, "PortcallControllerV1Test");
        dataUpdatedTime = metadataUpdated.toString();
    }

    @Test
    public void listAllPortCalls() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS + "?vesselName=test")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listPortCallsFromLocodeByVesselName() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS + "?locode=FIHEL&vesselName=test")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllPortCallsFromLocodeSucceeds() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS + "?locode=FIHEL")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listPortsSucceeds() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORTS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.dataUpdatedTime", is(dataUpdatedTime)))
            .andExpect(jsonPath("$.ssnLocations.features", hasSize(PORT_LOCATIONS)))
            .andExpect(jsonPath("$.portAreas.features", hasSize(OTHER_PORTS+FIHEL_PORTS)))
            .andExpect(jsonPath("$.berths.berths", hasSize(FIHEL_BERTHS+OTHER_BERTHS)))
        ;
    }

    @Test
    public void listPortsWithLocodeSucceeds() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORTS + "/FIHEL")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.dataUpdatedTime", is(dataUpdatedTime)))
            .andExpect(jsonPath("$.ssnLocations.features", hasSize(1)))
            .andExpect(jsonPath("$.portAreas.features", hasSize(FIHEL_PORTS)))
            .andExpect(jsonPath("$.berths.berths", hasSize(FIHEL_BERTHS)))

        ;
    }

    @Test
    public void listPortsWithUnknownLocodeFails() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORTS + "/" + UNKNOWN_LOCODE)))
            .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void listCodeDescriptionsSucceeds() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.CODE_DESCRIPTIONS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.dataUpdatedTime", is(dataUpdatedTime)))
            .andExpect(jsonPath("$.cargoTypes", hasSize(0)))
            .andExpect(jsonPath("$.vesselTypes", hasSize(20)))
            .andExpect(jsonPath("$.agentTypes", hasSize(3)))
        ;
    }

    @Test
    public void listVesselDetailsSucceeds() throws Exception {
        logDebugResponse(
            mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.VESSEL_DETAILS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
        ;
    }
}
