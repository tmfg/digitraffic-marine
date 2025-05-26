package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails;
import okhttp3.mockwebserver.MockWebServer;

public class VesselDetailsUpdaterTest extends AbstractDaemonTestBase {
    
    private VesselDetailsUpdater vesselDetailsUpdater;
    private MockWebServer server;

    @Autowired
    private VesselDetailsRepository vesselDetailsRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private WebClient portnetWebClient;

    @BeforeEach
    public void before() {
        server = new MockWebServer();
        final VesselDetailsClient vesselDetailsClient = new VesselDetailsClient(server.url("/vesselDetailsUrl/").toString(), portnetWebClient);
        vesselDetailsUpdater = new VesselDetailsUpdater(vesselDetailsRepository, vesselDetailsClient, updatedTimestampRepository);
    }

    @Test
    @Transactional
    @Rollback
    public void updateVesselDetailsSucceeds() throws IOException, InterruptedException {
        addXmlResponseFromFile(server, "vesselDetails/vesselDetailsResponse1.xml");
        addXmlResponseFromFile(server, "vesselDetails/vesselDetailsResponse2.xml");

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 29, 6, 30, 59, 0, ZoneOffset.UTC);

        vesselDetailsUpdater.updateVesselDetails(from);
        expectResponse(server, "/vesselDetailsUrl/fromDte=20160129&fromTme=063059");

        final List<VesselDetails> vessels1 = vesselDetailsRepository.findByVesselIdInOrderByVesselIdAsc(Arrays.asList(358L, 4637L,
            99995524L,
            99995388L));

        assertEquals(4, vessels1.size());
        assertEquals(358L, vessels1.get(0).getVesselId().longValue());
        assertEquals("Oulu", vessels1.get(0).getVesselRegistration().getPortOfRegistry());
        assertEquals(93, vessels1.get(0).getVesselConstruction().getVesselTypeCode().intValue());
        assertEquals("CUST", vessels1.get(0).getVesselSystem().getShipVerifier());
        assertEquals(1042, vessels1.get(0).getVesselDimensions().getGrossTonnage().intValue());

        vesselDetailsUpdater.updateVesselDetails(from);
        expectResponse(server, "/vesselDetailsUrl/fromDte=20160129&fromTme=063059");

        final List<VesselDetails> vessels2 = vesselDetailsRepository.findByVesselIdInOrderByVesselIdAsc(Arrays.asList(358L, 4637L,
            99995524L, 99995388L));

        assertEquals(4, vessels2.size());
        assertEquals(358L, vessels2.get(0).getVesselId().longValue());
        assertEquals("Turku", vessels2.get(0).getVesselRegistration().getPortOfRegistry());
        assertEquals(95, vessels2.get(0).getVesselConstruction().getVesselTypeCode().intValue());
        assertEquals("CUST", vessels2.get(0).getVesselSystem().getShipVerifier());
        assertEquals(666, vessels2.get(0).getVesselDimensions().getGrossTonnage().intValue());
    }
}
