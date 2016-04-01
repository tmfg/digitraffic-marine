package fi.livi.digitraffic.meri.service;

import com.mysema.query.types.expr.BooleanExpression;
import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.QVesselLocation;
import fi.livi.digitraffic.meri.domain.VesselLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VesselLocationServiceImpl implements VesselLocationService {
    private final VesselLocationRepository vesselLocationRepository;

    @Autowired
    public VesselLocationServiceImpl(final VesselLocationRepository vesselLocationRepository) {
        this.vesselLocationRepository = vesselLocationRepository;
    }

    @Override
    public Iterable<VesselLocation> findLocations(final int mmsi, final Long from, final Long to) {

        QVesselLocation vesselLocation = QVesselLocation.vesselLocation;
        BooleanExpression predicate = vesselLocation.mmsi.eq(mmsi);
        if (from != null) {
            predicate = predicate.and(vesselLocation.timestamp.goe(from));
        }
        if (to != null) {
            predicate = predicate.and(vesselLocation.timestamp.loe(to));
        }

        return vesselLocationRepository.findAll(predicate);
    }

    @Override
    public Iterable<VesselLocation> findLocations(final Long from, final Long to) {
        QVesselLocation vesselLocation = QVesselLocation.vesselLocation;

        BooleanExpression predicate = QVesselLocation.vesselLocation.isNotNull();

        if (from != null) {
            predicate = vesselLocation.timestamp.goe(from);
        }
        if (from != null) {
            predicate = predicate == null ?
                    vesselLocation.timestamp.loe(to) :
                    predicate.and(vesselLocation.timestamp.loe(to));
        }

        return vesselLocationRepository.findAll(predicate);
    }
}
