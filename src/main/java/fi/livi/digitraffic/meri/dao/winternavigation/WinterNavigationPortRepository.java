package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPort;
import jakarta.persistence.QueryHint;

@Repository
public interface WinterNavigationPortRepository extends JpaRepository<WinterNavigationPort, String> {

    @Query(value = "UPDATE WINTER_NAVIGATION_PORT SET obsolete_date = CURRENT_TIMESTAMP WHERE locode NOT IN (:locodes)", nativeQuery = true)
    @Modifying
    void setRemovedPortsObsolete(@Param("locodes") final List<String> locodes);

    @QueryHints({ @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "1000") })
    @EntityGraph(attributePaths = { "portRestrictions" })
    Stream<WinterNavigationPort> findDistinctByObsoleteDateIsNullOrderByLocode();
}
