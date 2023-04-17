package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.transform.StringSource;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayPointRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirway;
import ibnet_baltice_schema.WaypointsResponseType;
import ibnet_baltice_waypoints.DirWaysType;

public class WinterNavigationDirwayUpdaterTest extends AbstractTestBase {

    @MockBean
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationDirwayUpdater winterNavigationDirwayUpdater;

    @Autowired
    private WinterNavigationDirwayRepository winterNavigationDirwayRepository;

    @Autowired
    private WinterNavigationDirwayPointRepository winterNavigationDirwayPointRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    @BeforeEach
    public void before() {
        winterNavigationDirwayUpdater = new WinterNavigationDirwayUpdater(winterNavigationClient, winterNavigationDirwayRepository,
                                                                          winterNavigationDirwayPointRepository, updatedTimestampRepository);
        winterNavigationDirwayRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationDirwaysSucceeds() throws IOException {
        when(winterNavigationClient.getWinterNavigationWaypoints()).thenReturn(getResponse("winterNavigationDirwaysResponse.xml"));

        winterNavigationDirwayUpdater.updateWinterNavigationDirways();

        List<WinterNavigationDirway> dirways = winterNavigationDirwayRepository.findDistinctByOrderByName();
        assertEquals(12, dirways.size());

        final WinterNavigationDirway dirway = dirways.stream().filter(d -> d.getName().equals("VTS test dirway")).findFirst().get();
        assertEquals(ZonedDateTime.parse("2017-12-08T07:28:51.342+00:00").toEpochSecond(), dirway.getIssueTime().toEpochSecond());
        assertEquals(4, dirway.getDirwayPoints().size());
        assertEquals(64.967740173, dirway.getDirwayPoints().get(2).getLatitude(), 0.00000001);
        assertEquals(23.131164517, dirway.getDirwayPoints().get(2).getLongitude(), 0.00000001);
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationDirwaysChangedSucceeds() throws IOException {
        when(winterNavigationClient.getWinterNavigationWaypoints()).thenReturn(getResponse("winterNavigationDirwaysResponse.xml"));
        winterNavigationDirwayUpdater.updateWinterNavigationDirways();

        when(winterNavigationClient.getWinterNavigationWaypoints()).thenReturn(getResponse("winterNavigationDirwaysResponse2.xml"));
        winterNavigationDirwayUpdater.updateWinterNavigationDirways();

        final List<WinterNavigationDirway> dirways = winterNavigationDirwayRepository.findDistinctByOrderByName();
        assertEquals(11, dirways.size());

        final WinterNavigationDirway dirway = dirways.stream().filter(d -> d.getName().equals("VTS test dirway")).findFirst().get();
        assertEquals(ZonedDateTime.parse("2017-12-08T08:28:51.342+00:00").toEpochSecond(), dirway.getIssueTime().toEpochSecond());
        assertEquals("IBNEXT Center Issuer", dirway.getIssuerName());
        assertEquals(4, dirway.getDirwayPoints().size());
        assertEquals(4, dirway.getDirwayPoints().get(3).getWinterNavigationDirwayPointPK().getOrderNumber());
        assertEquals(66.176112382, dirway.getDirwayPoints().get(3).getLatitude(), 0.00000001);
        assertEquals(23.570617642, dirway.getDirwayPoints().get(3).getLongitude(), 0.00000001);
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationDirwaysEmpty() throws IOException {
        when(winterNavigationClient.getWinterNavigationWaypoints()).thenReturn(getResponse("winterNavigationDirwaysResponse_empty.xml"));

        winterNavigationDirwayUpdater.updateWinterNavigationDirways();

        final List<WinterNavigationDirway> dirways = winterNavigationDirwayRepository.findDistinctByOrderByName();
        assertEquals(0, dirways.size());

    }

    private DirWaysType getResponse(final String filename) throws IOException {
        final JAXBElement<WaypointsResponseType> unmarshal =
            ((JAXBElement<WaypointsResponseType>) jaxb2Marshaller.unmarshal(new StringSource(readFile(filename))));
        return unmarshal.getValue().getWaypoints();
    }
}
