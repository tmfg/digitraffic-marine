package fi.livi.digitraffic.meri.dao.winternavigation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayPointPK;

public interface WinterNavigationDirwayPointRepository extends JpaRepository<WinterNavigationDirwayPoint, WinterNavigationDirwayPointPK> {

    @Modifying
    @Query(value = "DELETE FROM WINTER_NAVIGATION_DIRWAY_POINT WHERE dirway_name NOT IN (:names)", nativeQuery = true)
    void deleteAllNotIn(@Param(value = "names") final List<String> names);
}
