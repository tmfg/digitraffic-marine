package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.config.MarineCacheConfiguration.CACHE_ALLOWED_MMSI;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselMetadataJsonV1;
import fi.livi.digitraffic.meri.model.ais.VesselMetadata;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;
import jakarta.persistence.EntityManager;

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

    @Transactional(readOnly = true)
    public VesselMetadataJsonV1 findAllowedMetadataByMssi(final int mmsi) {
        final VesselMetadataJsonV1 metadata = vesselMetadataRepository.findByMmsi(mmsi);

        if(metadata == null || FORBIDDEN_SHIP_TYPES.contains(metadata.getShipType())) {
            throw new ObjectNotFoundException("VesselMetadata", mmsi);
        }

        return metadata;
    }

    @Transactional(readOnly = true)
    public List<VesselMetadataJsonV1> findAllowedVesselMetadataFrom(final Long from, final Long to) {
        final QueryBuilder<VesselMetadataJsonV1, VesselMetadata> qb = new QueryBuilder<>(entityManager, VesselMetadataJsonV1.class, VesselMetadata.class);

        qb.notIn("shipType", FORBIDDEN_SHIP_TYPES);

        if(from != null) {
            qb.gte(qb.get("timestamp"), from);
        }

        if(to != null) {
            qb.lte(qb.get("timestamp"), to);
        }

        return qb.getResults();
    }

    @Transactional(readOnly = true)
    @Cacheable(CACHE_ALLOWED_MMSI)
    public Collection<Integer> findAllowedMmsis() {
        final QueryBuilder<Integer, VesselMetadata> qb = new QueryBuilder<>(entityManager, Integer.class, VesselMetadata.class);

        qb.notIn("shipType", FORBIDDEN_SHIP_TYPES);

        return qb.getResults("mmsi");
    }

    @Transactional
    public void saveVessels(final List<VesselMessage> messages) {
        final List<VesselMetadata> vessels = messages.stream()
            .map(v -> new VesselMetadata(v.vesselAttributes))
            .collect(Collectors.toList());

        vesselMetadataRepository.saveAll(vessels);
    }
}
