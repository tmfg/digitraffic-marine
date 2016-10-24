package fi.livi.digitraffic.meri.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaKey;
import fi.livi.digitraffic.meri.model.portnet.PortAreaJson;

@Repository
public interface PortAreaRepository extends JpaRepository<PortArea, PortAreaKey> {
    @Query(value = "select locode, port_area_code, port_area_name from port_area", nativeQuery = true)
    Stream<PortAreaJson> streamAllPortArea();
}
