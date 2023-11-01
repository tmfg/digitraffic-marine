package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.dto.ais.external.VesselMessage.VesselAttributes;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.model.ais.VesselMetadata;
import jakarta.transaction.Transactional;

@Transactional
public class VesselMetadataServiceTest extends AbstractDaemonTestBase {

    private static final Logger log = LoggerFactory.getLogger(VesselMetadataServiceTest.class);

    @SpyBean
    private VesselMetadataService vesselMetadataService;

    @Autowired
    private VesselMetadataRepository vesselMetadataRepository;

    @Test
    public void testAllowedMmsisCache() throws Exception {
        vesselMetadataRepository.save(createNewVesselMetadata(-1, 30));
        vesselMetadataRepository.save(createNewVesselMetadata(-2, 40));

        Mockito.verify(vesselMetadataService, Mockito.times(0)).findAllowedMmsis();
        Collection<Integer> initial = vesselMetadataService.findAllowedMmsis();
        Mockito.verify(vesselMetadataService, Mockito.times(1)).findAllowedMmsis();
        assertFalse(initial.contains(-1));
        assertTrue(initial.contains(-2));

        vesselMetadataRepository.save(createNewVesselMetadata(-3, 30));
        vesselMetadataRepository.save(createNewVesselMetadata(-4, 40));

        Mockito.verify(vesselMetadataService, Mockito.times(1)).findAllowedMmsis();

        // from cache
        Collection<Integer> cache = vesselMetadataService.findAllowedMmsis();
        Mockito.verify(vesselMetadataService, Mockito.times(1)).findAllowedMmsis();
        assertFalse(cache.contains(-1));
        assertTrue(cache.contains(-2));
        assertFalse(cache.contains(-3));
        assertFalse(cache.contains(-4));

        log.info("Wait for cache to expire");
        Thread.sleep(400);

        // from cache
        Collection<Integer> cache2 = vesselMetadataService.findAllowedMmsis();
        Mockito.verify(vesselMetadataService, Mockito.times(2)).findAllowedMmsis();
        assertFalse(cache2.contains(-1));
        assertTrue(cache2.contains(-2));
        assertFalse(cache2.contains(-3));
        assertTrue(cache2.contains(-4));
    }

    private static VesselMetadata createNewVesselMetadata(final int mmsi, int type) {
        return new VesselMetadata(
                new VesselAttributes(
                mmsi,
                1,//int imo,
                1,//long timestampExt,
                UUID.randomUUID().toString(),//String callSign,
                UUID.randomUUID().toString(),//String vesselName,
                type,//int shipAndCargoType,
                1,//long referencePointA,
                1,//long referencePointB,
                1,//long referencePointC,
                1,//long referencePointD,
                1,//int posType,
                1,//int eta,
                1,//int draught,
                UUID.randomUUID().toString()));//String dest)
    }
}
