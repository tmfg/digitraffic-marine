package fi.livi.digitraffic.meri.service.ais;

import static fi.livi.digitraffic.meri.service.ais.VesselMetadataService.FORBIDDEN_SHIP_TYPES;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;
import fi.livi.digitraffic.meri.util.dao.QueryBuilder;

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

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocations(final Integer mmsi, final Long from, final Long to) {
        return VesselLocationConverter.createFeatureCollection(findLocations(mmsi, from, to));
    }

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocationsWithinRadiusFromPoint(final double radius, final double latitude,
                                                                                     final double longitude, final Long from, final Long to) {
        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, latitude, longitude, from, to, FORBIDDEN_SHIP_TYPES));
    }

    @Transactional(readOnly = true)
    public VesselLocationFeatureCollection findAllowedLocationsWithinRadiusFromMMSI(final double radius, final int mmsi, final long from) {
        final VesselLocation location = vesselLocationRepository.findByMmsi(mmsi);
        if (location == null) {
            throw new ObjectNotFoundException(VesselLocation.class, mmsi);
        }

        return VesselLocationConverter.createFeatureCollection(
                vesselLocationRepository.findAllVesselsWithinRadiusFromPoint(radius, location.getY(), location.getX(), from, null, FORBIDDEN_SHIP_TYPES));
    }

    private List<VesselLocation> findLocations(final Integer mmsi, final Long from, final Long to) {
        final QueryBuilder<VesselLocation, VesselLocation> qb = new QueryBuilder<>(entityManager, VesselLocation.class, VesselLocation.class);
        final Subquery<Integer> subquery = getMmsiSubQuery(qb, mmsi);

        if (mmsi != null) {
            qb.equals("mmsi", mmsi);
        }

        if (from != null) {
            qb.gte(qb.get("timestampExternal"), from);
        }

        if (to != null) {
            qb.lte(qb.get("timestampExternal"), to);
        }

        qb.in("mmsi", subquery);

        return qb.getResults();
    }

    private Subquery<Integer> getMmsiSubQuery(final QueryBuilder qb, final Integer mmsi)  {
        final Subquery<Integer> subquery = qb.subquery(Integer.class);
        final Root<VesselMetadata> root = subquery.from(VesselMetadata.class);
        final List<Predicate> predicateList = new ArrayList<>();

        predicateList.add(qb.inPredicate(root.get("shipType"), FORBIDDEN_SHIP_TYPES).not());

        if (mmsi != null) {
            predicateList.add(qb.equalPredicate(root.get("mmsi"), mmsi));
        }

        subquery.select(root.get("mmsi"))
                .where(predicateList.toArray(new Predicate[] {}));

        return subquery;
    }

    @Transactional
    public void saveLocations(final List<AISMessage> messages) {
        final List<VesselLocation> locations = messages.stream().map(VesselLocation::new).collect(Collectors.toList());

        vesselLocationRepository.saveAll(locations);
    }
}
