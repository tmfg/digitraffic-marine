package fi.livi.digitraffic.meri.service.ais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;

@Service
@Transactional(readOnly = true)
public class VesselMetadataService {
    private final VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    public VesselMetadataService(final VesselMetadataRepository vesselMetadataRepository) {
        this.vesselMetadataRepository = vesselMetadataRepository;
    }

    public VesselMetadataJson findMetadataByMssi(final int mmsi) {
        return vesselMetadataRepository.findByMmsi(mmsi);
    }

    public List<VesselMetadataJson> listAllVesselMetadata() {
        return vesselMetadataRepository.findAllProjectedBy();
    }
}
