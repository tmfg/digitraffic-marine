package fi.livi.digitraffic.meri.service.ais;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.util.SublistFetcher;

@Service
@Transactional(readOnly = true)
public class VesselMetadataService {

    // 30 = fishing boat
    public static final Collection<Integer> FORBIDDEN_SHIP_TYPES = Collections.singletonList(30);

    private EntityManager entityManager;
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

    public List<VesselMetadataJson> listAllowedVesselMetadata() {
        final Criteria c = createCriteria().setProjection(Projections.id());
        c.add(getAllowedShipTypeCriteria());
        return SublistFetcher.fetch(c.list(), vesselMetadataRepository::findByMmsiIn);
    }

    public Collection<Integer> findAllowedMmsis() {
        final Criteria c = createCriteria().setProjection(Projections.id());
        c.add(getAllowedShipTypeCriteria());
        return c.list();
    }

    public static Criterion getAllowedShipTypeCriteria() {
        return Restrictions.not(Restrictions.in("shipType", FORBIDDEN_SHIP_TYPES));
    }
}
