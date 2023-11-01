package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.controller.portcall.PortcallControllerV1;

public class PortcallControllerTest extends AbstractWebTestBase {

    @Test
    public void listAllPortCalls() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS +
                "?vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listPortCallsFromLocodeByVesselName() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS +
                "?locode=FIHEL&vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllPortCallsFromLocodeSucceeds() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS + "?locode=FIHEL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listCodeDescriptions() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.CODE_DESCRIPTIONS))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("agentTypes", Matchers.notNullValue()))
            .andExpect(jsonPath("agentTypes[0].code", Matchers.notNullValue()))
            .andExpect(jsonPath("agentTypes[0].descriptionFi", Matchers.notNullValue()))
            .andExpect(jsonPath("agentTypes[0].descriptionEn", Matchers.notNullValue()))
            .andExpect(jsonPath("cargoTypes", Matchers.empty()))
            .andExpect(jsonPath("agentTypes[0].descriptionEn", Matchers.notNullValue()))
            .andExpect(jsonPath("vesselTypes", Matchers.notNullValue()))
            .andExpect(jsonPath("vesselTypes[0].code", Matchers.notNullValue()))
            .andExpect(jsonPath("vesselTypes[0].descriptionFi", Matchers.notNullValue()))
            .andExpect(jsonPath("agentTypes[0].descriptionEn", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllMetadata() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 +
                PortcallControllerV1.PORTS))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("ssnLocations", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocations.features[0]", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocations.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationByLocode() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 +
                PortcallControllerV1.PORTS +
                "/FIHKO"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("ssnLocations", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocations.features[0]", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocations.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationByLocodeInvalidLocation() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 +
                PortcallControllerV1.PORTS +
                "/FIZZY"))
            .andExpect(status().is(404))
        ;
    }

    @Test
    public void findVesselDetailsNoParameters() throws Exception {
        mockMvc.perform(get(PortcallControllerV1.API_PORT_CALL_V1 +
                PortcallControllerV1.VESSEL_DETAILS))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(content().string("[ ]"))
        ;
    }

}
