package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.model.portnet.SsnLocationJson;

@Repository
public interface SsnLocationRepository extends JpaRepository<SsnLocation, String> {
    @Query(value = "select locode, location_name, country, wgs84_lat, wgs84_long from ssn_location", nativeQuery = true)
    Stream<SsnLocationJson> streamAllLocations();
}
