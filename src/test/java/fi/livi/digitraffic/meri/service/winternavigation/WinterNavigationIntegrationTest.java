package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.service.winternavigation.v1.WinterNavigationWebServiceV1;
import ibnet_baltice_waypoints.DirWaysType;
import ibnet_baltice_winterships.WinterShips;

@Disabled("For manual integration testing")
public class WinterNavigationIntegrationTest extends AbstractDaemonTestBase {
    @Autowired
    private WinterNavigationClient winterNavigationClient;

    @Autowired
    private WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Autowired
    private WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Autowired
    private WinterNavigationDirwayUpdater winterNavigationDirwayUpdater;

    @Autowired
    private WinterNavigationWebServiceV1 winterNavigationWebServiceV1;

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationPortsSucceeds() {
        final Instant start = Instant.now().minusSeconds(1);

        int count = winterNavigationPortUpdater.updateWinterNavigationPorts();
        assertTrue(count > 100, "Total count of added or updated ports is greater than 100");

        final Instant dataUpdatedTime = winterNavigationWebServiceV1.getWinterNavigationPorts().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationShipsSucceeds() {
        final Instant start = Instant.now().minusSeconds(1);

        final WinterShips ships = winterNavigationClient.getWinterNavigationShips();

        assertNotNull(ships);
        assertTrue(ships.getWinterShip().size() > 100);

        int count = winterNavigationShipUpdater.updateWinterNavigationShips();
        assertTrue(count > 100, "Total count of added or updated ships is greater than 100");

        final Instant dataUpdatedTime = winterNavigationWebServiceV1.getWinterNavigationShips().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }

    @Test
    @Disabled("For manual integration testing")
    public void getWinterNavigationWaypointsSucceeds() {
        final Instant start = Instant.now().minusSeconds(1);

        final DirWaysType dirways = winterNavigationClient.getWinterNavigationWaypoints();

        assertNotNull(dirways);
        assertTrue(!dirways.getDirWay().isEmpty());

        int count = winterNavigationDirwayUpdater.updateWinterNavigationDirways();
        assertTrue(count > 0, "Total count of added or updated dirways is greater than 0");

        Instant dataUpdatedTime = winterNavigationWebServiceV1.getWinterNavigationDirways().getDataUpdatedTime();

        assertNotNull(dataUpdatedTime);
        assertTrue(dataUpdatedTime.isAfter(start));
    }
}
