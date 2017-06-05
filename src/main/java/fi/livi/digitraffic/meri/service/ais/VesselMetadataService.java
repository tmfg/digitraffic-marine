package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.config.AisCacheConfiguration.ALLOWED_MMSI_CACHE;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;

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

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class)
                .createCriteria(VesselMetadata.class)
                .setFetchSize(1000);
    }


    public VesselMetadataJson findAllowedMetadataByMssi(final int mmsi) {
        final VesselMetadataJson metadata = vesselMetadataRepository.findByMmsi(mmsi);

        if(metadata == null || FORBIDDEN_SHIP_TYPES.contains(metadata.getShipType())) {
            throw new ObjectNotFoundException("VesselMetadata", mmsi);
        }

        return metadata;
    }

    public List<VesselMetadataJson> findAllowedVesselMetadataFrom(final Long from) {
        final Criteria c = createCriteria();
        c.add(getAllowedShipTypeCriteria());
        if (from != null) {
            c.add(Restrictions.ge("timestamp", from));
        }
        List<VesselMetadata> vesselMetadata = c.list();
        return vesselMetadata.stream().map(vessel -> (VesselMetadataJson) vessel).collect(Collectors.toList());
    }

    @Cacheable(ALLOWED_MMSI_CACHE)
    public Collection<Integer> findAllowedMmsis() {
        final Criteria c = createCriteria().setProjection(Projections.id());
        c.add(getAllowedShipTypeCriteria());
        return c.list();
    }

    public static Criterion getAllowedShipTypeCriteria() {
        return Restrictions.not(Restrictions.in("shipType", FORBIDDEN_SHIP_TYPES));
    }
}
