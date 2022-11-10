package fi.livi.digitraffic.meri.dao;

import static java.time.ZoneOffset.UTC;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.dto.info.v1.DataSourceInfoDtoV1;
import fi.livi.digitraffic.meri.model.DataSource;
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
        WINTER_NAVIGATION_DIRWAYS,

        BRIDGE_LOCK_DISRUPTIONS,
        ATON_FAULTS
    }

    enum JsonCacheKey {
        NAUTICAL_WARNINGS_ACTIVE("nautical-warnings-active"),
        NAUTICAL_WARNINGS_ARCHIVED("nautical-warnings-archived");

        private final String key;

        JsonCacheKey(final String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    @Modifying
    @Query(value =
        "insert into updated_timestamp(updated_name, updated_time, updated_by) values(:name, :time, :by)\n" +
        "on conflict (updated_name)\n" +
        "do update set\n" +
        "   updated_time = :time,\n" +
        "   updated_by = :by", nativeQuery = true)
    void setUpdated(final String name,
                    final Instant time,
                    final String by);

    default void setUpdated(final UpdatedName name,
                            final Instant time,
                            final String by) {
        setUpdated(name.name(), time, by);
    }

    default void setUpdated(final UpdatedName name,
                            final ZonedDateTime time,
                            final String by) {
        setUpdated(name.name(), time.toInstant(), by);
    }

    @Query(value = "select updated_time from updated_timestamp where updated_name = :name", nativeQuery = true)
    Instant findLastUpdatedInstant(final String name);

    @Query(value = "select updated_time from updated_timestamp where updated_name = :#{#updatedName.name()}", nativeQuery = true)
    Instant findLastUpdatedInstant(final UpdatedName updatedName);

    default ZonedDateTime findLastUpdated(final UpdatedName name) {
        final Instant i = findLastUpdatedInstant(name.name());

        return i == null ? null : i.atZone(UTC);
    }

    @Query(value =
        "select si.id, si.source, si.update_interval as updateInterval\n" +
        "from data_source_info si\n" +
        "WHERE id = :#{#dataSource.name()}", nativeQuery = true)
    DataSourceInfoDtoV1 getDataSourceInfo(final DataSource dataSource);


    default Duration getDataSourceUpdateInterval(final DataSource dataSource) {
        return Optional.ofNullable(getDataSourceInfo(dataSource))
            .flatMap(sourceInfoDtoV1 -> Optional.ofNullable(sourceInfoDtoV1 != null ?
                                                            sourceInfoDtoV1.getUpdateInterval() :
                                                            null))
            .orElse(null);
    }

    @Query(value =
        "select max(j.last_updated)\n" +
        "from cached_json j\n" +
        "where j.cache_id in (:#{#cacheKeys.![getKey()]})", nativeQuery = true)
    Instant getNauticalWarningsLastUpdated(final JsonCacheKey...cacheKeys);
}
