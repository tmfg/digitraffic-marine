package fi.livi.digitraffic.meri.dao.ais;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;

public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer> {
    List<VesselLocationJson> findByMmsiIn(final List<Integer> ids);
}
