package fi.livi.digitraffic.meri.dao.winternavigation;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirwayPointPK;

public interface WinterNavigationDirwayPointRepository extends JpaRepository<WinterNavigationDirwayPoint, WinterNavigationDirwayPointPK> {

}
