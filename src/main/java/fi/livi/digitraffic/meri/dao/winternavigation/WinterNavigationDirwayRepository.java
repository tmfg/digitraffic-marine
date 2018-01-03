package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirway;

@Repository
public interface WinterNavigationDirwayRepository extends JpaRepository<WinterNavigationDirway, String> {

    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "1000") })
    @EntityGraph(attributePaths = { "dirwayPoints" })
    List<WinterNavigationDirway> findDistinctByOrderByName();
}
