package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.config.MarineCacheConfiguration.CACHE_ALLOWED_MMSI;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;

@Service
@Transactional(readOnly = true)
public class VesselMetadataService {
    // 30 = fishing boat
    public static final List<Integer> FORBIDDEN_SHIP_TYPES = Collections.singletonList(30);

    private final EntityManager entityManager;
    private final VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    public VesselMetadataService(final EntityManager entityManager,
                                 final VesselMetadataRepository vesselMetadataRepository) {
        this.entityManager = entityManager;
        this.vesselMetadataRepository = vesselMetadataRepository;
    }

    public VesselMetadataJson findAllowedMetadataByMssi(final int mmsi) {
        final VesselMetadataJson metadata = vesselMetadataRepository.findByMmsi(mmsi);

        if(metadata == null || FORBIDDEN_SHIP_TYPES.contains(metadata.getShipType())) {
            throw new ObjectNotFoundException("VesselMetadata", mmsi);
        }

        return metadata;
    }

    public List<VesselMetadataJson> findAllowedVesselMetadataFrom(final Long from) {
        final QueryBuilder<VesselMetadataJson, VesselMetadata> qb = new QueryBuilder<>(entityManager, VesselMetadataJson.class, VesselMetadata.class);

        qb.notIn("shipType", FORBIDDEN_SHIP_TYPES);

        if(from != null) {
            qb.gte(qb.get("timestamp"), from);
        }

        return qb.getResults();
    }

    @Cacheable(CACHE_ALLOWED_MMSI)
    public Collection<Integer> findAllowedMmsis() {
        final QueryBuilder<Integer, VesselMetadata> qb = new QueryBuilder<>(entityManager, Integer.class, VesselMetadata.class);

        qb.notIn("shipType", FORBIDDEN_SHIP_TYPES);

        return qb.getResults("mmsi");
    }
}
