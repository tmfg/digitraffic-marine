package fi.livi.digitraffic.meri.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;

@Service
@Transactional(readOnly = true)
public class VesselLocationService {
    private final EntityManager entityManager;

    @Autowired
    public VesselLocationService(final EntityManager entityManager) {
        this.entityManager = entityManager;
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

    public List<VesselLocationJson> findLocations(final Long from, final Long to) {
        return findLocations(null, from, to);
    }
}
