package fi.livi.digitraffic.meri.dao.winternavigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;

@Repository
public interface WinterNavigationRepository extends JpaRepository<WinterNavigationPort, String> {

}
