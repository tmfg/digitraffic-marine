package fi.livi.digitraffic.meri;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.ResourceAccessException;

import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.controller.VesselLocationController;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

public class DefaultExceptionHandlerTest extends AbstractTestBase {
    @MockBean
    private VesselLocationService vesselLocationService;

    private ResultActions performQuery() throws Exception {
        return mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_LOCATIONS_PATH +
            VesselLocationController.LATEST_PATH + "/1"));
    }

    @Test
    public void ok() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), nullable(Long.class), nullable(Long.class))).thenReturn(new VesselLocationFeatureCollection(null));

        performQuery()
            .andExpect(status().isOk());

    }

    private <T extends Throwable> void testException(final Class<T> clazz, final int statuscode) throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), nullable(Long.class), nullable(Long.class))).thenThrow(clazz);

        performQuery()
            .andExpect(status().is(statuscode));

    }

    @Test
    public void notFoundException() throws Exception {
        testException(ObjectNotFoundException.class, 404);
    }

    @Test
    public void badRequestException() throws Exception {
        testException(BadRequestException.class, 400);
    }

    @Test
    public void resourceAccessException() throws Exception {
        testException(ResourceAccessException.class, 500);
    }

    @Test
    public void illegalArgumentException() throws Exception {
        testException(IllegalArgumentException.class, 400);
    }
    
    @Test
    public void httpMessageNotWritableException() throws Exception {
        testException(HttpMessageNotWritableException.class, 500);
    }

    @Test
    public void httpMessageNotReadableException() throws Exception {
        testException(HttpMessageNotReadableException.class, 400);
    }
}
