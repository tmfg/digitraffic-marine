package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.transform.StringSource;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShip;
import ibnet_baltice_schema.WinterShipsResponseType;
import ibnet_baltice_winterships.WinterShips;
import jakarta.xml.bind.JAXBElement;

public class WinterNavigationShipUpdaterTest extends AbstractDaemonTestBase {

    @MockitoBean
    private WinterNavigationClient winterNavigationClient;

    @MockitoBean(answers = Answers.CALLS_REAL_METHODS)
    private WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Autowired
    private WinterNavigationShipRepository winterNavigationShipRepository;

    @Autowired
    private UpdaterService updaterService;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    @BeforeEach
    public void before() {
        winterNavigationShipUpdater = new WinterNavigationShipUpdater(winterNavigationClient, updaterService);
        winterNavigationShipRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationShipsSucceeds() throws IOException {
        when(winterNavigationClient.getWinterNavigationShips())
            .thenReturn(getResponse("winternavigation/winterNavigationShipsResponse1.xml"))
            .thenReturn(getResponse("winternavigation/winterNavigationShipsResponse2.xml"));

        winterNavigationShipUpdater.updateWinterNavigationShips();

        final List<WinterNavigationShip> shipsList = winterNavigationShipRepository.findDistinctByOrderByVesselPK().collect(Collectors.toList());
        WinterNavigationShip ship = shipsList.stream().filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertEquals(1802, shipsList.size());
        assertEquals("9386524", ship.getImo());
        assertEquals("A La Marine", ship.getName());
        assertEquals("HK", ship.getNatCode());
        assertEquals(171.0, ship.getAisLength(), 0.1);
        assertEquals("moored", ship.getShipState().getAisStateText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipState().getTimestamp().toEpochSecond());
        assertEquals(0.00, ship.getShipState().getSpeed(), 0.1);

        assertEquals(1, ship.getShipActivities().size());
        assertEquals("PORT", ship.getShipActivities().iterator().next().getActivityType());
        assertEquals("In port", ship.getShipActivities().iterator().next().getActivityText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipActivities().iterator().next().getBeginTime().toEpochSecond());
        assertEquals(0, ship.getShipPlannedActivities().size());

        winterNavigationShipUpdater.updateWinterNavigationShips();

        final Stream<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();
        ship = ships.filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertEquals("A La Marine", ship.getName());
        assertEquals(4.00, ship.getShipState().getSpeed(), 0.1);
        assertEquals("under way using engine", ship.getShipState().getAisStateText());

        assertEquals(1, ship.getShipActivities().size());
        assertEquals("FREE", ship.getShipActivities().iterator().next().getActivityType());
        assertEquals("Moving freely", ship.getShipActivities().iterator().next().getActivityText());
        assertEquals(ZonedDateTime.parse("2017-12-13T12:50:03.000+00:00").toEpochSecond(), ship.getShipActivities().iterator().next().getBeginTime().toEpochSecond());
    }

    private WinterShips getResponse(final String filename) throws IOException {
        final JAXBElement<WinterShipsResponseType> unmarshal =
            ((JAXBElement<WinterShipsResponseType>) jaxb2Marshaller.unmarshal(new StringSource(readFile(filename))));
        return unmarshal.getValue().getWinterShips();
    }
}
