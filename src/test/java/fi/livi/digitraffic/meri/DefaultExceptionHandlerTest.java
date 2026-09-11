package fi.livi.digitraffic.meri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import fi.livi.digitraffic.meri.controller.ais.AisControllerV1;
import fi.livi.digitraffic.meri.controller.exception.DefaultExceptionHandler;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselLocationFeatureCollectionV1;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@TestPropertySource(properties = {
    "marine.datasource.hikari.maximum-pool-size=1",
})
@ExtendWith(MockitoExtension.class)
public class DefaultExceptionHandlerTest extends AbstractWebTestBase {
    private MockMvc mockMvc;
    private DefaultExceptionHandler exceptionHandler;
    @Mock
    private VesselLocationService vesselLocationService;

    @Mock
    private Logger exceptionHandlerLogger;

    @InjectMocks
    private AisControllerV1 aisControllerV1;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new DefaultExceptionHandler(exceptionHandlerLogger);
        // Mockito does not handle the @Validated annotation on AisControllerV1 correctly, because it uses aspects.
        // Therefore we create a mock instance of AisControllerV1 manually and give it only the ExceptionHandler
        // dependency.
        mockMvc = MockMvcBuilders.standaloneSetup(aisControllerV1)
            .setControllerAdvice(exceptionHandler)
            .build();
        lenient().when(exceptionHandlerLogger.isErrorEnabled()).thenReturn(true);
        lenient().when(exceptionHandlerLogger.isInfoEnabled()).thenReturn(true);
    }

    private ResultActions performQuery() throws Exception {
        return this.mockMvc.perform(get(AisControllerV1.API_AIS_V1 + AisControllerV1.LOCATIONS + "?mmsi=1"));
    }

    @Test
    public void ok() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), nullable(Long.class), nullable(Long.class))).thenReturn(new VesselLocationFeatureCollectionV1(null, null));

        performQuery()
            .andExpect(status().isOk());

    }

    private enum LogMode {
        ERROR, INFO, NONE
    }

    private <T extends Throwable> void testException(final Class<T> clazz, final int statuscode, final LogMode logMode) throws Exception {
        final T throwable = mock(clazz);

        testException(throwable, statuscode, logMode);
    }

    private <T extends Throwable> void testException(final T throwable, final int statuscode, final LogMode logMode) throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), nullable(Long.class), nullable(Long.class))).thenAnswer(i -> { throw throwable; } );

        performQuery()
            .andExpect(status().is(statuscode));

        switch (logMode) {
            case ERROR -> {
                verify(exceptionHandlerLogger).error(anyString(), any(Throwable.class));
                verify(exceptionHandlerLogger, times(0)).info(anyString(), any(Throwable.class));
            }
            case INFO -> {
                verify(exceptionHandlerLogger).info(anyString(), any(Throwable.class));
                verify(exceptionHandlerLogger, times(0)).error(anyString(), any(Throwable.class));
            }
            case NONE -> {
                verify(exceptionHandlerLogger, times(0)).error(anyString(), any(Throwable.class));
                verify(exceptionHandlerLogger, times(0)).info(anyString(), any(Throwable.class));
            }
        }
    }

    @Test
    public void notFoundException() throws Exception {
        testException(ObjectNotFoundException.class, 404, LogMode.NONE);
    }

    @Test
    public void badRequestException() throws Exception {
        testException(BadRequestException.class, 400, LogMode.NONE);
    }

    @Test
    public void resourceAccessException() throws Exception {
        testException(ResourceAccessException.class, 500, LogMode.ERROR);
    }

    @Test
    public void illegalArgumentException() throws Exception {
        testException(IllegalArgumentException.class, 400, LogMode.INFO);
    }

    @Test
    public void constraintViolationException() throws Exception {
        testException(ConstraintViolationException.class, 400, LogMode.INFO);
    }

    @Test
    public void httpMessageNotWritableException() throws Exception {
        testException(HttpMessageNotWritableException.class, 500, LogMode.INFO);
    }

    @Test
    public void httpMessageNotReadableException() throws Exception {
        testException(HttpMessageNotReadableException.class, 400, LogMode.INFO);
    }

    @Test
    public void methodArgumentTypeMismatchException() throws Exception {
        testException(MethodArgumentTypeMismatchException.class, 400, LogMode.INFO);
    }

    @Test
    public void typeMismatchException() throws Exception {
        testException(TypeMismatchException.class, 400, LogMode.INFO);
    }

    @Test
    public void missingServletRequestParameterException() throws Exception {
        testException(MissingServletRequestParameterException.class, 400, LogMode.INFO);
    }

    @Test
    public void conversionFailedException() throws Exception {
        testException(ConversionFailedException.class, 400, LogMode.INFO);
    }

    @Test
    public void invalidParameterException() throws Exception {
        testException(InvalidParameterException.class, 400, LogMode.INFO);
    }

    @Test
    public void httpMediaTypeNotAcceptableException() throws Exception {
        testException(HttpMediaTypeNotAcceptableException.class, 406, LogMode.INFO);
    }

    @Test
    public void httpRequestMethodNotSupportedException() throws Exception {
        testException(HttpRequestMethodNotSupportedException.class, 405, LogMode.INFO);
    }

    @Test
    public void httpMediaTypeNotSupportedException() throws Exception {
        testException(HttpMediaTypeNotSupportedException.class, 500, LogMode.INFO);
    }

    @Test
    public void noResourceFoundException() throws Exception {
        testException(NoResourceFoundException.class, 404, LogMode.INFO);
    }

    @Test
    public void clientAbortIOException() throws Exception {
        testException(new org.apache.catalina.connector.ClientAbortException("aborted"), 200, LogMode.NONE);
    }

    @Test
    public void exceptionFallback() throws Exception {
        testException(Exception.class, 500, LogMode.INFO);
    }
}
