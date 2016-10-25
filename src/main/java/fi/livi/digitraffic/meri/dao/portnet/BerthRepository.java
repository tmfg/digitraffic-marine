package fi.livi.digitraffic.meri.dao.portnet;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.BerthKey;
import fi.livi.digitraffic.meri.model.portnet.BerthJson;

@Repository
public interface BerthRepository extends JpaRepository<Berth, BerthKey> {
    @Query(value = "select locode, port_area_code, berth_code, berth_name from berth", nativeQuery = true)
    Stream<BerthJson> streamAllBerth();
}
