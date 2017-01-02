package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.model.ais.VesselMessage.VesselAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;

@Transactional
public class VesselMetadataServiceTest extends AbstractIntegrationTest {

    @Autowired
    private VesselMetadataService vesselMetadataService;

    @Autowired
    private VesselMetadataRepository vesselMetadataRepository;

    @Test
    public void testCache() throws IOException, InterruptedException {

        vesselMetadataRepository.save(createNewVesselMetadata(-1, 30));
        vesselMetadataRepository.save(createNewVesselMetadata(-2, 40));

        Collection<Integer> initial = vesselMetadataService.findAllowedMmsis();
        Assert.assertFalse(initial.contains(-1));
        Assert.assertTrue(initial.contains(-2));

        vesselMetadataRepository.save(createNewVesselMetadata(-3, 30));
        vesselMetadataRepository.save(createNewVesselMetadata(-4, 40));

        // from cache
        Collection<Integer> cache = vesselMetadataService.findAllowedMmsis();
        Assert.assertFalse(cache.contains(-1));
        Assert.assertTrue(cache.contains(-2));
        Assert.assertFalse(cache.contains(-3));
        Assert.assertFalse(cache.contains(-4));

        Thread.sleep(1000*60);

        // from cache
        Collection<Integer> cache2 = vesselMetadataService.findAllowedMmsis();
        Assert.assertFalse(cache2.contains(-1));
        Assert.assertTrue(cache2.contains(-2));
        Assert.assertFalse(cache2.contains(-3));
        Assert.assertTrue(cache2.contains(-4));
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
