package fi.livi.digitraffic.meri.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.ApiConstants;
import fi.livi.digitraffic.meri.controller.ais.AisControllerV1;
import fi.livi.digitraffic.meri.controller.portcall.PortcallControllerV1;
import fi.livi.digitraffic.meri.controller.sse.SseControllerV1;
import fi.livi.digitraffic.meri.controller.winternavigation.WinterNavigationControllerV1;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.dto.info.v1.DataSourceInfoDtoV1;
import fi.livi.digitraffic.meri.dto.info.v1.UpdateInfoDtoV1;
import fi.livi.digitraffic.meri.dto.info.v1.UpdateInfosDtoV1;
import fi.livi.digitraffic.meri.model.DataSource;
import fi.livi.digitraffic.meri.util.TimeUtil;

@Service
public class DataStatusService {

    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final VesselLocationRepository vesselLocationRepository;

    @Autowired
    public DataStatusService(final UpdatedTimestampRepository updatedTimestampRepository,
                             final VesselLocationRepository vesselLocationRepository) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.vesselLocationRepository = vesselLocationRepository;
    }

//    @Transactional
//    public void updateDataUpdated(final DataType dataType) {
//        dataUpdatedRepository.upsertDataUpdated(dataType);
//    }
//
//    @Transactional
//    public void updateDataUpdated(final DataType dataType, final String subtype) {
//        dataUpdatedRepository.upsertDataUpdated(dataType, subtype);
//    }
//
//    @Transactional
//    public void updateDataUpdated(final DataType dataType, final Instant updated) {
//        dataUpdatedRepository.upsertDataUpdated(dataType, updated);
//    }

//    @Transactional(readOnly = true)
//    public ZonedDateTime findDataUpdatedTime(final DataType dataType) {
//        return DateHelper.toZonedDateTimeAtUtc(dataUpdatedRepository.findUpdatedTime(dataType));
//    }
//
//    @Transactional(readOnly = true)
//    public Instant findDataUpdatedInstant(final DataType dataType) {
//        return dataUpdatedRepository.findUpdatedTime(dataType);
//    }
//
//    @Transactional(readOnly = true)
//    public Instant findDataUpdatedTime(final DataType dataType, final List<String> subtypes) {
//        return dataUpdatedRepository.findUpdatedTime(dataType, subtypes);
//    }

//    @Transactional(readOnly = true)
//    public Instant getTransactionStartTime() {
//        return updatedTimestampRepository.getTransactionStartTime();
//    }

    @Transactional(readOnly = true)
    public UpdateInfosDtoV1 getUpdatedInfos() {

        final List<UpdateInfoDtoV1> updatedInfos =
            Stream.of(
                getSseUpdateInfos().stream(),
                getPortCallUpdateInfos().stream(),
                getVesselsUpdateInfos().stream(),
                getWinterNavigationPortsUpdateInfos().stream(),
                getNauticalWarningssUpdateInfos().stream(),
                getBridgeLockisruptionUpdateInfos().stream(),
                getAtonFaultUpdateInfos().stream()
            )
                .flatMap(Function.identity())
                .sorted(Comparator.comparing(o -> o.api))
                .collect(Collectors.toList());

        final Instant max =
            updatedInfos.stream()
                .map(updateInfoDtoV1 -> TimeUtil.getGreatest(updateInfoDtoV1.getDataUpdatedTime(), updateInfoDtoV1.dataCheckedTime))
                .filter(Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(Instant.EPOCH);
        return new UpdateInfosDtoV1(updatedInfos, max);
    }

    private List<UpdateInfoDtoV1> getSseUpdateInfos() {
        // /api/sse/v1/measurements
        return Collections.singletonList(
            new UpdateInfoDtoV1(SseControllerV1.API_SSE_V1 + SseControllerV1.MEASUREMENTS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.SSE_DATA),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.SSE_DATA))
        );
    }

    private List<UpdateInfoDtoV1> getPortCallUpdateInfos() {

        return Arrays.asList(
            // /api/port-call/v1/vessel-details
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_CALLS),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.PORT_CALL)),
            // /api/port-call/v1/port-calls
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.CODE_DESCRIPTIONS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_METADATA),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.PORT_CALL_CODE_DESCRIPTIONS)),
            // /api/port-call/v1/locations (ssnLocation, portArea, berthRepository)
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORTS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_METADATA),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.PORT_CALL_LOCATION)),
            // /api/port-call/v1/code-descriptions
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.VESSEL_DETAILS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.VESSEL_DETAILS),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.PORT_CALL_VESSEL_DETAIL))
        );
    }

    private List<UpdateInfoDtoV1> getVesselsUpdateInfos() {
        return Arrays.asList(
            // /api/ais/v1/vessels
            new UpdateInfoDtoV1(AisControllerV1.API_AIS_V1 + AisControllerV1.VESSELS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.VESSEL_DETAILS),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.VESSEL_DETAIL)),
            // /api/ais/v1/locations
            new UpdateInfoDtoV1(AisControllerV1.API_AIS_V1 + AisControllerV1.LOCATIONS,
                                vesselLocationRepository.getLastModified(),
                                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.VESSEL_LOCATION))
        );
    }

    private List<UpdateInfoDtoV1> getWinterNavigationPortsUpdateInfos() {
        return Arrays.asList(
            // /api/winter-navigation/v1/ports
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.PORTS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.WINTER_NAVIGATION_PORT)),
            // /api/winter-navigation/v1/vessels
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.VESSELS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_SHIPS),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.WINTER_NAVIGATION_VESSEL)),
            // /api/winter-navigation/v1/dirways
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.DIRWAYS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.WINTER_NAVIGATION_DIRWAY))
        );
    }

    private List<UpdateInfoDtoV1> getNauticalWarningssUpdateInfos() {
        // /api/nautical-warning/v1/warnings/active
        // /api/nautical-warning/v1/warnings/archived
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_NAUTICAL_WARNING_V1_WARNINGS,
                updatedTimestampRepository.getNauticalWarningsLastUpdated(UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ACTIVE,
                                                                          UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ARCHIVED),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.NAUTICAL_WARNING))
        );
    }

    private List<UpdateInfoDtoV1> getBridgeLockisruptionUpdateInfos() {
        // /api/bridge-lock/v1/disruptions
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_BRIDGE_LOCK_V1_DISRUPTIONS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.BRIDGE_LOCK_DISRUPTIONS),
                 null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.BRIDGE_LOCK_DISRUPTION))
        );
    }

    private List<UpdateInfoDtoV1> getAtonFaultUpdateInfos() {
        // /api/aton/v1/faults
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_ATON_V1_FAULTS,
                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.ATON_FAULTS),
                null, updatedTimestampRepository.getDataSourceUpdateInterval(DataSource.ATON_FAULTS))
        );
    }

    @Transactional(readOnly = true)
    public DataSourceInfoDtoV1 getDataSourceInfo(final DataSource dataSource) {
        return updatedTimestampRepository.getDataSourceInfo(dataSource);
    }

    @Transactional(readOnly = true)
    public Duration getDataSourceUpdateInterval(final DataSource dataSource) {
        return updatedTimestampRepository.getDataSourceUpdateInterval(dataSource);
    }

}
