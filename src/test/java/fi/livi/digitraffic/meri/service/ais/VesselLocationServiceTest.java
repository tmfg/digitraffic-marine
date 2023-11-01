package fi.livi.digitraffic.meri.service.ais;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselLocationFeatureCollectionV1;
import fi.livi.digitraffic.meri.model.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.VesselMetadata;

public class VesselLocationServiceTest extends AbstractWebTestBase {

    private class Point {
        public final double x;
        public final double y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Autowired
    private VesselLocationRepository vesselLocationRepository;

    @Autowired
    private VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    private VesselLocationService vesselLocationService;

    private static final long now = ZonedDateTime.now().toEpochSecond();

    private static final int MMSItoBeFound = 666;
    private static final int MMSI = 696;

    private final Point p1 = new Point(20, 60);
    private final Point p2 = new Point(71, 17);

    // Distance between 60 N, 20 E and 17 N, 71 E = 6265.9 km
    private final double radius1 = 6265;
    private final double radius2 = 6266;

    private final VesselMetadata vesselToBeFoundMetadata = new VesselMetadata(new VesselMessage.VesselAttributes(
            MMSItoBeFound, 1, 2, "callSign", "vesselName", 3, 4,
            5, 6, 7,8, 9, 10, "dest"
    ));

    private final VesselLocation vesselToBeFound = new VesselLocation(
            new AISMessage(new AISMessage.Geometry(p1.x, p1.y, null),
                           new AISMessage.AISAttributes(MMSItoBeFound,
                                                        now,
                                                        1, 2, 3, 4, 5, 6, 7,
                                                        now)));

    @BeforeEach
    public void before() {
        vesselMetadataRepository.save(vesselToBeFoundMetadata);
        vesselLocationRepository.save(vesselToBeFound);
    }

    @Test
    @Transactional
    @Rollback
    public void findVesselsWithinRadiusSucceeds() {
        final VesselLocationFeatureCollectionV1
                featureCollection1 = vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius1, p2.y, p2.x, now, null);

        assertFalse(featureCollection1.getFeatures().stream().anyMatch(v -> v.mmsi == MMSItoBeFound),
            "Vessel should not be found withing radius " + radius1);

        final VesselLocationFeatureCollectionV1
                featureCollection2 = vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius2, p2.y, p2.x, now, null);

        assertTrue(featureCollection2.getFeatures().stream().anyMatch(v -> v.mmsi == MMSItoBeFound),
            "Vessel should be found withing radius " + radius2);
    }

    @Test
    @Transactional
    @Rollback
    public void findVesselsWithinRadiusFromMMSISucceeds() {

        final VesselMetadata metadata = new VesselMetadata(new VesselMessage.VesselAttributes(
                MMSI, 1, 2, "callSign", "vesselName", 3, 4,
                5, 6, 7,8, 9, 10, "dest"
        ));

        vesselMetadataRepository.save(metadata);

        final VesselLocation vessel = new VesselLocation(
                new AISMessage(new AISMessage.Geometry(p2.x, p2.y, null),
                               new AISMessage.AISAttributes(MMSI,
                                                            now,
                                                            1, 2, 3, 4, 5, 6, 7,
                                                            now)));
        vesselLocationRepository.save(vessel);

        VesselLocationFeatureCollectionV1 featureCollection = vesselLocationService.findAllowedLocationsWithinRadiusFromMMSI(radius1, MMSI, now);

        assertFalse(featureCollection.getFeatures().stream().anyMatch(v -> v.mmsi == MMSItoBeFound),
            "Vessel should not be found withing radius " + radius1);

        featureCollection = vesselLocationService.findAllowedLocationsWithinRadiusFromMMSI(radius2, MMSI, now);

        assertTrue(featureCollection.getFeatures().stream().anyMatch(v -> v.mmsi == MMSItoBeFound),
            "Vessel should be found withing radius " + radius2);
    }
}
