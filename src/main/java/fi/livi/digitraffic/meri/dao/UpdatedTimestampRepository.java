package fi.livi.digitraffic.meri.dao;

import static java.time.ZoneOffset.UTC;

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
        PORT_CALLS_CHECK,
        PORT_CALLS_TO,
        PORT_METADATA,
        PORT_VESSEL_DETAILS,
        PORT_VESSEL_DETAILS_CHECK,
        SSE_DATA,
        WINTER_NAVIGATION_PORTS,
        WINTER_NAVIGATION_PORTS_CHECK,
        WINTER_NAVIGATION_VESSELS,
        WINTER_NAVIGATION_VESSELS_CHECK,
        WINTER_NAVIGATION_DIRWAYS,
        WINTER_NAVIGATION_DIRWAYS_CHECK,

        BRIDGE_LOCK_DISRUPTIONS,
        BRIDGE_LOCK_DISRUPTIONS_CHECK,
        ATON_FAULTS,
        ATON_FAULTS_CHECK
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
        "select si.id" +
        "     , si.source" +
        "     , si.update_interval as updateInterval\n" +
        "     , si.recommended_fetch_interval as recommendedFetchInterval\n" +
        "from data_source_info si\n" +
        "WHERE id = :#{#dataSource.name()}", nativeQuery = true)
    DataSourceInfoDtoV1 getDataSourceInfo(final DataSource dataSource);


    default String getDataSourceUpdateInterval(final DataSource dataSource) {
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

    @Query(value =
               "select max(j.modified)\n" +
               "from cached_json j\n" +
               "where j.cache_id in (:#{#cacheKeys.![getKey()]})", nativeQuery = true)
    Instant getNauticalWarningsLastModified(final JsonCacheKey...cacheKeys);

    @Query(value =
               "select max(av.modified)\n" +
               "from aton_fault av\n", nativeQuery = true)
    Instant getAtonVaultsLastModified();
}
