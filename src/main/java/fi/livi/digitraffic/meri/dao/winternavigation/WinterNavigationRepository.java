package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;

@Repository
public interface WinterNavigationRepository extends JpaRepository<WinterNavigationPort, String> {

    @Query(value = "UPDATE WINTER_NAVIGATION_PORT SET obsolete_date = sysdate WHERE locode NOT IN (:locodes)", nativeQuery = true)
    @Modifying
    void setRemovedPortsObsolete(@Param("locodes") final List<String> locodes);

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "1000"))
    @EntityGraph(attributePaths = { "portRestrictions" })
    List<WinterNavigationPort> findDistinctLocodeByObsoleteDateIsNullOrderByLocode();
}
