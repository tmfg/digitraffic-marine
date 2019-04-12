package fi.livi.digitraffic.meri.dao;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.util.dao.SqlRepository;

@Repository
public interface UpdatedTimestampRepository extends SqlRepository {
    enum UpdatedName {
        PORT_CALLS,
        PORT_METADATA,
        SSE_DATA,
        VESSEL_DETAILS,
        WINTER_NAVIGATION_PORTS,
        WINTER_NAVIGATION_SHIPS,
        WINTER_NAVIGATION_DIRWAYS
    }

    @Modifying
    @Query(value = "insert into updated_timestamp(updated_name, updated_time, updated_by) values(:name, :time, :by)\n" +
        "on conflict (updated_name)\n" +
        "do update set\n" +
        "   updated_time = :time,\n" +
        "   updated_by = :by", nativeQuery = true)
    void setUpdated(@Param("name")final String name, @Param("time")final ZonedDateTime time, @Param("by")final String by);

    default void setUpdated(@Param("name")final UpdatedName name, @Param("time")final ZonedDateTime time, @Param("by")final String by) {
        setUpdated(name.name(), time, by);
    }

    @Query(value = "select updated_time from updated_timestamp where updated_name = :name", nativeQuery = true)
    Instant findLastUpdatedInstant(@Param("name") final String name);

    default ZonedDateTime findLastUpdated(final UpdatedName name) {
        final Instant i = findLastUpdatedInstant(name.name());

        return i == null ? null : i.atZone(UTC);
    }
}
