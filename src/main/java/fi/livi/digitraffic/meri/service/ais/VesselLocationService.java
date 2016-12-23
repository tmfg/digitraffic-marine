package fi.livi.digitraffic.meri.service.ais;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;
import fi.livi.digitraffic.util.SublistFetcher;

@Service
@Transactional(readOnly = true)
public class VesselLocationService {
    private final EntityManager entityManager;
    private final VesselLocationRepository vesselLocationRepository;

    @Autowired
    public VesselLocationService(final EntityManager entityManager,
                                 final VesselLocationRepository vesselLocationRepository) {
        this.entityManager = entityManager;
        this.vesselLocationRepository = vesselLocationRepository;
    }

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class)
                .createCriteria(VesselLocation.class)
                .setFetchSize(1000);
    }

    public List<VesselLocationJson> findLocations(final int mmsi, final Long from, final Long to) {
        return findLocations((Integer)mmsi, from, to);
    }

    private List<VesselLocationJson> findLocations(final Integer mmsi, final Long from, final Long to) {
        final Criteria c = createCriteria().setProjection(Projections.id());

        if(mmsi != null) {
            c.add(Restrictions.eq("mmsi", mmsi));
        }

        if(from != null) {
            c.add(Restrictions.ge("timestamp", from));
        }

        if(to != null) {
            c.add(Restrictions.le("timestamp", from));
        }

        return SublistFetcher.fetch(c.list(), vesselLocationRepository::findByMmsiIn);
    }

    public List<VesselLocationJson> findLocations(final Long from, final Long to) {
        return findLocations(null, from, to);
    }
}
