package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.stream.Stream;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShip;
import jakarta.persistence.QueryHint;

public interface WinterNavigationShipRepository extends JpaRepository<WinterNavigationShip, String> {
    @QueryHints({ @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "300") })
    @EntityGraph(attributePaths = { "shipState", "shipVoyage", "shipActivities", "shipPlannedActivities" })
    Stream<WinterNavigationShip> findDistinctByOrderByVesselPK();
}
