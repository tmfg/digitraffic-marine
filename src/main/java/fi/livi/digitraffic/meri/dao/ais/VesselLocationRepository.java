package fi.livi.digitraffic.meri.dao.ais;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fi.livi.digitraffic.meri.domain.ais.VesselLocation;

public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer> {

    VesselLocation findByMmsi(final int mmsi);

    /**
     * @param radius Search radius in kilometers
     * @param from Return only vessel locations that have been updated after given timestamp (milliseconds from epoch)
     * @return Vessel locations matching given parameters
     */
    @Query(value = "SELECT vl.* FROM VESSEL_LOCATION vl " +
                   "WHERE EXISTS(SELECT 1 FROM VESSEL v WHERE v.mmsi = vl.mmsi AND v.ship_type NOT IN (?5)) " +
                   "AND great_circle_distance(vl.y, vl.x, ?2, ?3) <= ?1 " +
                   "AND timestamp_ext >= ?4", nativeQuery = true)
    List<VesselLocation> findAllVesselsWithinRadiusFromPoint(final double radius, final double latitude, final double longitude, long from,
                                                             Collection<Integer> forbiddenShipTypes);
}
