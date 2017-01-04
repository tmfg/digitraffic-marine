package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import fi.livi.digitraffic.meri.domain.portnet.VesselDetails.VesselDetails;

public class VesselDetailsUpdaterTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private VesselDetailsClient vesselDetailsClient;

    @Autowired
    private VesselDetailsUpdater vesselDetailsUpdater;

    @Autowired
    private VesselDetailsRepository vesselDetailsRepository;

    private MockRestServiceServer server;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void before() {
        server = MockRestServiceServer.createServer(restTemplate);
        Mockito.when(vesselDetailsClient.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updateVesselDetailsSucceeds() throws FileNotFoundException {

        String response = readFile("vesselDetailsResponse1.xml");

        server.expect(MockRestRequestMatchers.requestTo("nullfromDte=20160129&fromTme=063059"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        response = readFile("vesselDetailsResponse2.xml");

        server.expect(MockRestRequestMatchers.requestTo("nullfromDte=20160129&fromTme=063059"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        Instant from = ZonedDateTime.of(2016, 1, 29, 6, 30, 59, 0, ZoneId.systemDefault()).toInstant();

        vesselDetailsUpdater.updateVesselDetails(from);

        List<VesselDetails> vessels = vesselDetailsRepository.findByVesselIdInOrderByVesselIdAsc(Arrays.asList(358L, 4637L, 99995524L, 99995388L));

        assertEquals(4, vessels.size());
        assertEquals(358L, vessels.get(0).getVesselId().longValue());
        assertEquals("Oulu", vessels.get(0).getVesselRegistration().getPortOfRegistry());
        assertEquals(93, vessels.get(0).getVesselConstruction().getVesselTypeCode().intValue());
        assertEquals("CUST", vessels.get(0).getVesselSystem().getShipVerifier());
        assertEquals(1042, vessels.get(0).getVesselDimensions().getGrossTonnage().intValue());

        vesselDetailsUpdater.updateVesselDetails(from);
        server.verify();

        vessels = vesselDetailsRepository.findByVesselIdInOrderByVesselIdAsc(Arrays.asList(358L, 4637L, 99995524L, 99995388L));

        assertEquals(4, vessels.size());
        assertEquals(358L, vessels.get(0).getVesselId().longValue());
        assertEquals("Turku", vessels.get(0).getVesselRegistration().getPortOfRegistry());
        assertEquals(95, vessels.get(0).getVesselConstruction().getVesselTypeCode().intValue());
        assertEquals("CUST", vessels.get(0).getVesselSystem().getShipVerifier());
        assertEquals(666, vessels.get(0).getVesselDimensions().getGrossTonnage().intValue());
    }
}
