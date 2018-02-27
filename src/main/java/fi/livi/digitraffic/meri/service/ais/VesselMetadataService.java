package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.config.AisCacheConfiguration.ALLOWED_MMSI_CACHE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public VesselMetadataJson findAllowedMetadataByMssi(final int mmsi) {
        final VesselMetadataJson metadata = vesselMetadataRepository.findByMmsi(mmsi);

        if(metadata == null || FORBIDDEN_SHIP_TYPES.contains(metadata.getShipType())) {
            throw new ObjectNotFoundException("VesselMetadata", mmsi);
        }

        return metadata;
    }

    public List<VesselMetadataJson> findAllowedVesselMetadataFrom(final Long from) {
        final CriteriaBuilder cb = getCriteriaBuilder();
        final CriteriaQuery<VesselMetadataJson> query = cb.createQuery(VesselMetadataJson.class);
        final Root<VesselMetadata> root = query.from(VesselMetadata.class);
        final List<Predicate> predicateList = new ArrayList();

        predicateList.add(getAllowedShipTypeCriteria(root));

        if(from != null) {
            predicateList.add(cb.ge(root.get("timestamp"), from));
        }

        query.select(root)
             .where(predicateList.toArray(new Predicate[] {}));


        return entityManager.createQuery(query).getResultList();
    }

    @Cacheable(ALLOWED_MMSI_CACHE)
    public Collection<Integer> findAllowedMmsis() {
        final CriteriaQuery<Integer> query = getCriteriaBuilder().createQuery(Integer.class);
        final Root<VesselMetadata> root = query.from(VesselMetadata.class);

        query.select(root.get("mmsi"))
             .where(getAllowedShipTypeCriteria(root));

        return entityManager.createQuery(query).getResultList();
    }

    private Predicate getAllowedShipTypeCriteria(final Root<VesselMetadata> root) {
        return root.get("shipType").in(FORBIDDEN_SHIP_TYPES).not();
    }
}
