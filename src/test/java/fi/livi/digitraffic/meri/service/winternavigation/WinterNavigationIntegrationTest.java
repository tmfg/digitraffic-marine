package fi.livi.digitraffic.meri.service.winternavigation;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import ibnet_baltice_waypoints.DirWaysType;
import ibnet_baltice_winterships.WinterShips;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("For manual integration testing")
public class WinterNavigationIntegrationTest extends AbstractTestBase {
    @Autowired
    private WinterNavigationClient winterNavigationClient;

    @Autowired
    private WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Autowired
    private WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Autowired
    private WinterNavigationDirwayUpdater winterNavigationDirwayUpdater;

    @Autowired
    private WinterNavigationService winterNavigationService;

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationPortsSucceeds() throws IOException {
        final ZonedDateTime start = ZonedDateTime.now().minusSeconds(1);

        int count = winterNavigationPortUpdater.updateWinterNavigationPorts();
        assertTrue(count > 100, "Total count of added or updated ports is greater than 100");

        final ZonedDateTime dataUpdatedTime = winterNavigationService.getWinterNavigationPorts().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationShipsSucceeds() throws IOException {
        final ZonedDateTime start = ZonedDateTime.now().minusSeconds(1);

        final WinterShips ships = winterNavigationClient.getWinterNavigationShips();

        assertNotNull(ships);
        assertTrue(ships.getWinterShip().size() > 100);

        int count = winterNavigationShipUpdater.updateWinterNavigationShips();
        assertTrue(count > 100, "Total count of added or updated ships is greater than 100");

        final ZonedDateTime dataUpdatedTime = winterNavigationService.getWinterNavigationShips().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationWaypointsSucceeds() {
        final ZonedDateTime start = ZonedDateTime.now().minusSeconds(1);

        final DirWaysType dirways = winterNavigationClient.getWinterNavigationWaypoints();

        assertNotNull(dirways);
        assertTrue(dirways.getDirWay().size() > 0);

        int count = winterNavigationDirwayUpdater.updateWinterNavigationDirways();
        assertTrue(count > 0, "Total count of added or updated dirways is greater than 0");

        ZonedDateTime dataUpdatedTime = winterNavigationService.getWinterNavigationDirways().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }
}
