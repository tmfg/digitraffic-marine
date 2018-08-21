package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;

public interface WinterNavigationShipRepository extends JpaRepository<WinterNavigationShip, String> {

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    @EntityGraph(attributePaths = { "shipState", "shipVoyage", "shipActivities", "shipPlannedActivities" })
    List<WinterNavigationShip> findDistinctByOrderByVesselPK();
}
