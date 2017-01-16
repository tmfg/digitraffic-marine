package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.service.ais.VesselMetadataService.getAllowedShipTypeCriteria;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;

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

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocations(final int mmsi, final Long from, final Long to) {
        return VesselLocationConverter.createFeatureCollection(findAllowedLocations((Integer)mmsi, from, to));
    }

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocations(final Long from, final Long to) {
        return VesselLocationConverter.createFeatureCollection(findAllowedLocations(null, from, to));
    }

    private List<VesselLocation> findAllowedLocations(final Integer mmsi, final Long from, final Long to) {
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

        final DetachedCriteria subQuery = DetachedCriteria.forClass(VesselMetadata.class)
                .add(getAllowedShipTypeCriteria())
                .setProjection(Projections.id());
        c.add(Property.forName("mmsi").in(subQuery));

        return c.list();
    }
}
