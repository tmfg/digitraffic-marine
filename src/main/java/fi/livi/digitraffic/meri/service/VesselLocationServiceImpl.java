package fi.livi.digitraffic.meri.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.VesselLocation;

@Service
@Transactional(readOnly = true)
public class VesselLocationServiceImpl implements VesselLocationService {
    private final VesselLocationRepository vesselLocationRepository;

    @Autowired
    public VesselLocationServiceImpl(final VesselLocationRepository vesselLocationRepository) {
        this.vesselLocationRepository = vesselLocationRepository;
    }

    @Override
    public List<VesselLocation> findLocations(final int mmsi, final long from, final long to) {
        return vesselLocationRepository.findByMmsiAndTimestampBetween(mmsi, from, to);
    }

    @Override
    public List<VesselLocation> findLocations(final long from, final long to) {
        return vesselLocationRepository.findByTimestampBetween(from, to);
    }
}
