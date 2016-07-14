package fi.livi.digitraffic.meri.service;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.VesselLocation;

@Service
@Transactional(readOnly = true)
public class VesselLocationServiceImpl implements VesselLocationService {
    private final EntityManager entityManager;

    @Autowired
    public VesselLocationServiceImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Criteria createCriteria() {
        return entityManager.unwrap(Session.class).createCriteria(VesselLocation.class)
                .setFetchSize(1000);
    }

    @Override
    public Iterable<VesselLocation> findLocations(final int mmsi, final Long from, final Long to) {
        return findLocations((Integer)mmsi, from, to);
    }

    private Iterable<VesselLocation> findLocations(final Integer mmsi, final Long from, final Long to) {
        final Criteria c = createCriteria();

        if(mmsi != null) {
            c.add(Restrictions.eq("mmsi", mmsi));
        }

        if(from != null) {
            c.add(Restrictions.ge("timestamp", from));
        }

        if(to != null) {
            c.add(Restrictions.le("timestamp", from));
        }

        return c.list();
    }

    @Override
    public Iterable<VesselLocation> findLocations(final Long from, final Long to) {
        return findLocations(null, from, to);
    }
}
