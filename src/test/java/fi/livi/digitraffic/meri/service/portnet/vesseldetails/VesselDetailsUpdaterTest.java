package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.util.web.Jax2bRestTemplate;

public class VesselDetailsUpdaterTest extends AbstractTestBase {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private VesselDetailsClient vesselDetailsClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private VesselDetailsUpdater vesselDetailsUpdater;

    @Autowired
    private VesselDetailsRepository vesselDetailsRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private Jax2bRestTemplate restTemplate;

    private MockRestServiceServer server;

    @Before
    public void before() {
        vesselDetailsClient = new VesselDetailsClient("vesselDetailsUrl/", restTemplate);
        vesselDetailsUpdater = new VesselDetailsUpdater(vesselDetailsRepository, vesselDetailsClient, updatedTimestampRepository);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updateVesselDetailsSucceeds() throws IOException {

        String response = readFile("vesselDetailsResponse1.xml");

        server.expect(MockRestRequestMatchers.requestTo("/vesselDetailsUrl/fromDte=20160129&fromTme=063059"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        response = readFile("vesselDetailsResponse2.xml");

        server.expect(MockRestRequestMatchers.requestTo("/vesselDetailsUrl/fromDte=20160129&fromTme=063059"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 29, 6, 30, 59, 0, ZoneOffset.UTC);

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
