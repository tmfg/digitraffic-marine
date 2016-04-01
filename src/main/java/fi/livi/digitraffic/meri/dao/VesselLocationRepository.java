package fi.livi.digitraffic.meri.dao;

import java.util.List;

import fi.livi.digitraffic.meri.domain.VesselLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface VesselLocationRepository extends JpaRepository<VesselLocation, Integer>, QueryDslPredicateExecutor {

    List<VesselLocation> findByMmsiAndTimestampBetween(final int mmsi, final Long from, final Long to);

    List<VesselLocation> findByTimestampBetween(final Long from, final Long to);
}
