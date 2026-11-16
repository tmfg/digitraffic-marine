package fi.livi.digitraffic.meri.controller.portcall;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.controller.MediaTypes;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PortCallControllerV2Test extends AbstractWebTestBase {
    private static final String UNKNOWN_LOCODE = "UNKNOWN_LOCODE";

    @Test
    void listPortsSucceeds() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS))
            .andExpect(status().isOk())
            .andExpect(header().stringValues("Cache-Control", "max-age=86400, public"))
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.ssnLocations.features", notNullValue()))
            .andExpect(jsonPath("$.portAreas.features", notNullValue()))
            .andExpect(jsonPath("$.berths.berths", notNullValue()));
    }

    @Test
    void listPortsFiltersByCountryAndIgnoresCountryCase() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS)
                .param("country", "fi"))
            .andExpect(status().isOk())
            .andExpect(header().stringValues("Cache-Control", "max-age=86400, public"))
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.ssnLocations.features[*].locode", hasItem("FIHEL")))
            .andExpect(jsonPath("$.portAreas.features", hasSize(252)))
            .andExpect(jsonPath("$.berths.berths", hasSize(117)));
    }

    @Test
    void listPortsFiltersByCountryDenmark() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS)
                .param("country", "de"))
            .andExpect(status().isOk())
            .andExpect(header().stringValues("Cache-Control", "max-age=86400, public"))
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.ssnLocations.features[*].locode", hasItem("DEHGE")))
            .andExpect(jsonPath("$.portAreas.features", hasSize(0)))
            .andExpect(jsonPath("$.berths.berths", hasSize(0)));
    }

    @Test
    void listPortsRejectsCountryWithInvalidLength() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS)
                .param("country", "FIN"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findPortByLocodeSucceeds() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS + "/FIHEL"))
            .andExpect(status().isOk())
            .andExpect(header().stringValues("Cache-Control", "max-age=86400, public"))
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.ssnLocations.features", hasSize(1)))
            .andExpect(jsonPath("$.ssnLocations.features[0].locode", is("FIHEL")));
    }

    @Test
    void findUnknownPortByLocodeReturnsNotFound() throws Exception {
        mockMvc.perform(get(PortCallControllerV2.API_PORT_CALL_V2 + PortCallControllerV2.PORTS + "/" + UNKNOWN_LOCODE))
            .andExpect(status().isNotFound());
    }
}
