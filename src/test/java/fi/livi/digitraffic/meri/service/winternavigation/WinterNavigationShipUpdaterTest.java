package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.transform.StringSource;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;
import ibnet_baltice_schema.WinterShipsResponseType;
import ibnet_baltice_winterships.WinterShips;

public class WinterNavigationShipUpdaterTest extends AbstractIntegrationTest {

    @MockBean
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Autowired
    private WinterNavigationShipRepository winterNavigationShipRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    @Before
    public void before() {
        winterNavigationShipUpdater = new WinterNavigationShipUpdater(winterNavigationClient, winterNavigationShipRepository, updatedTimestampRepository);
        winterNavigationShipRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationShipsSucceeds() throws IOException {

        when(winterNavigationClient.getWinterNavigationShips())
            .thenReturn(getResponse("winterNavigationShipsResponse1.xml"))
            .thenReturn(getResponse("winterNavigationShipsResponse2.xml"));

        winterNavigationShipUpdater.updateWinterNavigationShips();

        List<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();
        WinterNavigationShip ship = ships.stream().filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertEquals(1802, ships.size());
        assertEquals("9386524", ship.getImo());
        assertEquals("A La Marine", ship.getName());
        assertEquals("HK", ship.getNatCode());
        assertEquals(171.0, ship.getAisLength(), 0.1);
        assertEquals("moored", ship.getShipState().getAisStateText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipState().getTimestamp().toEpochSecond());
        assertEquals(0.00, ship.getShipState().getSpeed(), 0.1);

        assertEquals(1, ship.getShipActivities().size());
        assertEquals("PORT", ship.getShipActivities().get(0).getActivityType());
        assertEquals("In port", ship.getShipActivities().get(0).getActivityText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipActivities().get(0).getBeginTime().toEpochSecond());
        assertEquals(0, ship.getShipPlannedActivities().size());

        winterNavigationShipUpdater.updateWinterNavigationShips();

        ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();
        ship = ships.stream().filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertEquals("A La Marine", ship.getName());
        assertEquals(4.00, ship.getShipState().getSpeed(), 0.1);
        assertEquals("under way using engine", ship.getShipState().getAisStateText());

        assertEquals(1, ship.getShipActivities().size());
        assertEquals("FREE", ship.getShipActivities().get(0).getActivityType());
        assertEquals("Moving freely", ship.getShipActivities().get(0).getActivityText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipActivities().get(0).getBeginTime().toEpochSecond());
    }

    private WinterShips getResponse(final String filename) throws IOException {
        final JAXBElement<WinterShipsResponseType> unmarshal =
            ((JAXBElement<WinterShipsResponseType>) jaxb2Marshaller.unmarshal(new StringSource(readFile(filename))));
        return unmarshal.getValue().getWinterShips();
    }
}
