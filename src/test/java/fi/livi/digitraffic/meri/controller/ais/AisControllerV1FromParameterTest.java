package fi.livi.digitraffic.meri.controller.ais;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.VesselMetadata;
import jakarta.persistence.EntityManager;

/**
 * Integration tests for the default 'from' parameter (24h in the past) on AIS endpoints.
 */
@Transactional
@Rollback
public class AisControllerV1FromParameterTest extends AbstractWebTestBase {

    @Autowired
    private VesselLocationRepository vesselLocationRepository;

    @Autowired
    private VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    private EntityManager entityManager;

    private static final int RECENT_MMSI = 100001;
    private static final int OLD_MMSI = 100002;

    private static final long NOW_MS = System.currentTimeMillis();
    private static final long ONE_HOUR_AGO_MS = NOW_MS - Duration.ofHours(1).toMillis();
    private static final long TWO_DAYS_AGO_MS = NOW_MS - Duration.ofHours(48).toMillis();

    @Test
    public void locationsDefaultFromReturnsOnlyRecentData() throws Exception {
        insertVesselWithLocation(RECENT_MMSI, ONE_HOUR_AGO_MS);
        insertVesselWithLocation(OLD_MMSI, TWO_DAYS_AGO_MS);

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 + AisControllerV1.LOCATIONS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.features.length()", is(1)))
                .andExpect(jsonPath("$.features[0].mmsi", is(RECENT_MMSI)));
    }

    @Test
    public void locationsExplicitFromReturnsBothOldAndNew() throws Exception {
        insertVesselWithLocation(RECENT_MMSI, ONE_HOUR_AGO_MS);
        insertVesselWithLocation(OLD_MMSI, TWO_DAYS_AGO_MS);

        final long threeDaysAgo = NOW_MS - Duration.ofHours(72).toMillis();

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 + AisControllerV1.LOCATIONS + "?from=" + threeDaysAgo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.features.length()", is(2)));
    }

    @Test
    public void vesselsDefaultFromReturnsOnlyRecentData() throws Exception {
        insertVesselMetadata(RECENT_MMSI, ONE_HOUR_AGO_MS);
        insertVesselMetadata(OLD_MMSI, TWO_DAYS_AGO_MS);

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 + AisControllerV1.VESSELS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].mmsi", is(RECENT_MMSI)));
    }

    @Test
    public void vesselsExplicitFromReturnsBothOldAndNew() throws Exception {
        insertVesselMetadata(RECENT_MMSI, ONE_HOUR_AGO_MS);
        insertVesselMetadata(OLD_MMSI, TWO_DAYS_AGO_MS);

        final long threeDaysAgo = NOW_MS - Duration.ofHours(72).toMillis();

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 + AisControllerV1.VESSELS + "?from=" + threeDaysAgo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    private void insertVesselWithLocation(final int mmsi, final long timestampMs) {
        vesselMetadataRepository.save(createVesselMetadata(mmsi, timestampMs));
        vesselLocationRepository.save(createVesselLocation(mmsi, timestampMs));
        flushAndClear();
    }

    private void insertVesselMetadata(final int mmsi, final long timestampMs) {
        vesselMetadataRepository.save(createVesselMetadata(mmsi, timestampMs));
        flushAndClear();
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    private VesselMetadata createVesselMetadata(final int mmsi, final long timestampMs) {
        return new VesselMetadata(new VesselMessage.VesselAttributes(
                mmsi, 1, timestampMs, "CS", "Vessel" + mmsi, 70,
                10, 20, 5, 5, 1, 0, 50, "DEST"
        ));
    }

    private VesselLocation createVesselLocation(final int mmsi, final long timestampMs) {
        return new VesselLocation(new AISMessage(
                new AISMessage.Geometry(25.0, 60.0, null),
                new AISMessage.AISAttributes(mmsi, timestampMs, 5.0, 180.0, 0, 0, 1, 0, 0, timestampMs)
        ));
    }
}
