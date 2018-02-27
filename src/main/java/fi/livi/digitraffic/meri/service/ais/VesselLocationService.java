package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.service.ais.VesselMetadataService.FORBIDDEN_SHIP_TYPES;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

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

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
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
                                                                                     final double longitude, final long from) {
        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, latitude, longitude, from, FORBIDDEN_SHIP_TYPES));
    }

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocationsWithinRadiusFromMMSI(final double radius, final int mmsi, final long from) {
        final VesselLocation location = vesselLocationRepository.findByMmsi(mmsi);
        if (location == null) {
            throw new ObjectNotFoundException(VesselLocation.class, mmsi);
        }

        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, location.getY(), location.getX(), from, FORBIDDEN_SHIP_TYPES));
    }

    private List<VesselLocation> findAllowedLocations(final Integer mmsi, final Long from, final Long to) {
        final CriteriaBuilder cb = getCriteriaBuilder();
        final CriteriaQuery<VesselLocation> query = cb.createQuery(VesselLocation.class);
        final Root<VesselLocation> root = query.from(VesselLocation.class);
        final List<Predicate> predicateList = new ArrayList();
        final Subquery<Integer> subquery = getMmsiSubQuery(cb, query, mmsi);

        if(mmsi != null) {
            predicateList.add(cb.equal(root.get("mmsi"), mmsi));
        }

        if(from != null) {
            predicateList.add(cb.ge(root.get("timestampExternal"), from));
        }

        if(to != null) {
            predicateList.add(cb.le(root.get("timestampExternal"), to));
        }

        predicateList.add(cb.in(root.get("mmsi")).value(subquery));

        query.select(root)
             .where(predicateList.toArray(new Predicate[] {}));

        return entityManager.createQuery(query).getResultList();
    }

    private Subquery<Integer> getMmsiSubQuery(final CriteriaBuilder cb, final CriteriaQuery<VesselLocation> query, final Integer mmsi)  {
        final Subquery<Integer> subquery = query.subquery(Integer.class);
        final Root<VesselMetadata> root = subquery.from(VesselMetadata.class);
        final List<Predicate> predicateList = new ArrayList<>();

        predicateList.add(cb.in(root.get("shipType")).value(FORBIDDEN_SHIP_TYPES).not());

        if(mmsi != null) {
            predicateList.add(cb.equal(root.get("mmsi"), mmsi));
        }

        subquery.select(root.get("mmsi"))
                .where(predicateList.toArray(new Predicate[] {}));

        return subquery;
    }
}
