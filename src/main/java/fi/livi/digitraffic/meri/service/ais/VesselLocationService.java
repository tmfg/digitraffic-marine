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

import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;

@Service
@Transactional(readOnly = true)
public class VesselLocationService {
    private final EntityManager entityManager;
    private final VesselLocationRepository vesselLocationRepository;
    private final VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    public VesselLocationService(final EntityManager entityManager,
                                 final VesselLocationRepository vesselLocationRepository,
                                 final VesselMetadataRepository vesselMetadataRepository) {
        this.entityManager = entityManager;
        this.vesselLocationRepository = vesselLocationRepository;
        this.vesselMetadataRepository = vesselMetadataRepository;
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

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocationsWithinRadiusFromPoint(final double radius, final double latitude,
                                                                                     final double longitude, long from) {

        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, latitude, longitude, from,
                                                                             VesselMetadataService.FORBIDDEN_SHIP_TYPES));
    }

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocationsWithinRadiusFromMMSI(final double radius, final int mmsi, long from) {

        final VesselLocation location = vesselLocationRepository.findByMmsi(mmsi);
        if (location == null) {
            throw new ObjectNotFoundException(VesselLocation.class, mmsi);
        }

        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, location.getY(), location.getX(), from,
                                                                             VesselMetadataService.FORBIDDEN_SHIP_TYPES));
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
