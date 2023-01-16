package fi.livi.digitraffic.meri.service;

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
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.dto.info.v1.DataSourceInfoDtoV1;
import fi.livi.digitraffic.meri.dto.info.v1.UpdateInfoDtoV1;
import fi.livi.digitraffic.meri.dto.info.v1.UpdateInfosDtoV1;
import fi.livi.digitraffic.meri.model.DataSource;
import fi.livi.digitraffic.meri.util.TimeUtil;

@Service
public class DataStatusService {

    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final VesselLocationRepository vesselLocationRepository;
    private final VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    public DataStatusService(final UpdatedTimestampRepository updatedTimestampRepository,
                             final VesselLocationRepository vesselLocationRepository,
                             final VesselMetadataRepository vesselMetadataRepository) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.vesselLocationRepository = vesselLocationRepository;
        this.vesselMetadataRepository = vesselMetadataRepository;
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
        final DataSourceInfoDtoV1 info = getDataSourceInfo(DataSource.SSE_DATA);
        return Collections.singletonList(
            new UpdateInfoDtoV1(SseControllerV1.API_SSE_V1 + SseControllerV1.MEASUREMENTS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.SSE_DATA),
                                null, // Data is pushed to DT
                                info.getUpdateInterval(), info.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getPortCallUpdateInfos() {

        final DataSourceInfoDtoV1 portCallInfo = getDataSourceInfo(DataSource.PORT_CALL);
        final DataSourceInfoDtoV1 portCallCodeInfo = getDataSourceInfo(DataSource.PORT_CALL_CODE_DESCRIPTIONS);
        final DataSourceInfoDtoV1 portCallVesselDetailInfo = getDataSourceInfo(DataSource.PORT_CALL_VESSEL_DETAIL);
        final DataSourceInfoDtoV1 portCallLocationInfo = getDataSourceInfo(DataSource.PORT_CALL_LOCATION);

        return Arrays.asList(
        // /api/port-call/v1/vessel-details
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORT_CALLS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_CALLS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_CALLS_CHECK),
                                portCallInfo.getUpdateInterval(), portCallInfo.getRecommendedFetchInterval()),
            // /api/port-call/v1/port-calls
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.CODE_DESCRIPTIONS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_METADATA),
                                null, portCallCodeInfo.getUpdateInterval(), portCallCodeInfo.getRecommendedFetchInterval()),
            // /api/port-call/v1/locations (ssnLocation, portArea, berthRepository)
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.PORTS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_METADATA),
                                null, portCallLocationInfo.getUpdateInterval(), portCallLocationInfo.getRecommendedFetchInterval()),
            // /api/port-call/v1/code-descriptions
            new UpdateInfoDtoV1(PortcallControllerV1.API_PORT_CALL_V1 + PortcallControllerV1.VESSEL_DETAILS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_VESSEL_DETAILS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.PORT_VESSEL_DETAILS_CHECK),
                                portCallVesselDetailInfo.getUpdateInterval(), portCallVesselDetailInfo.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getVesselsUpdateInfos() {
        final DataSourceInfoDtoV1 vesselDetailInfo = getDataSourceInfo(DataSource.VESSEL_DETAIL);
        final DataSourceInfoDtoV1 vesselLocationInfo = getDataSourceInfo(DataSource.VESSEL_LOCATION);
        return Arrays.asList(
            // /api/ais/v1/vessels
            new UpdateInfoDtoV1(AisControllerV1.API_AIS_V1 + AisControllerV1.VESSELS,
                                vesselMetadataRepository.getLastModified(),
                                null,
                                vesselDetailInfo.getUpdateInterval(), vesselDetailInfo.getRecommendedFetchInterval()),
            // /api/ais/v1/locations
            new UpdateInfoDtoV1(AisControllerV1.API_AIS_V1 + AisControllerV1.LOCATIONS,
                                vesselLocationRepository.getLastModified(),
                                null, vesselLocationInfo.getUpdateInterval(), vesselLocationInfo.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getWinterNavigationPortsUpdateInfos() {
        final DataSourceInfoDtoV1 portInfo = getDataSourceInfo(DataSource.WINTER_NAVIGATION_PORT);
        final DataSourceInfoDtoV1 vesselInfo = getDataSourceInfo(DataSource.WINTER_NAVIGATION_VESSEL);
        final DataSourceInfoDtoV1 dirwayInfo = getDataSourceInfo(DataSource.WINTER_NAVIGATION_DIRWAY);

        return Arrays.asList(
            // /api/winter-navigation/v1/ports
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.PORTS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS_CHECK),
                                portInfo.getUpdateInterval(), portInfo.getRecommendedFetchInterval()),
            // /api/winter-navigation/v1/vessels
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.VESSELS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_VESSELS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_VESSELS_CHECK),
                                vesselInfo.getUpdateInterval(), vesselInfo.getRecommendedFetchInterval()),
            // /api/winter-navigation/v1/dirways
            new UpdateInfoDtoV1(WinterNavigationControllerV1.API_WINTER_NAVIGATION_V1 + WinterNavigationControllerV1.DIRWAYS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS_CHECK),
                                dirwayInfo.getUpdateInterval(), dirwayInfo.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getNauticalWarningssUpdateInfos() {
        // /api/nautical-warning/v1/warnings/active
        // /api/nautical-warning/v1/warnings/archived
        final DataSourceInfoDtoV1 info = getDataSourceInfo(DataSource.NAUTICAL_WARNING);
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_NAUTICAL_WARNING_V1_WARNINGS,
                                updatedTimestampRepository.getNauticalWarningsLastModified(UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ACTIVE,
                                                                                           UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ARCHIVED),
                                updatedTimestampRepository.getNauticalWarningsLastUpdated(UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ACTIVE,
                                                                                          UpdatedTimestampRepository.JsonCacheKey.NAUTICAL_WARNINGS_ARCHIVED),
                                info.getUpdateInterval(), info.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getBridgeLockisruptionUpdateInfos() {
        // /api/bridge-lock/v1/disruptions
        final DataSourceInfoDtoV1 info = getDataSourceInfo(DataSource.BRIDGE_LOCK_DISRUPTION);
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_BRIDGE_LOCK_V1_DISRUPTIONS,
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.BRIDGE_LOCK_DISRUPTIONS),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.BRIDGE_LOCK_DISRUPTIONS_CHECK),
                                info.getUpdateInterval(), info.getRecommendedFetchInterval())
        );
    }

    private List<UpdateInfoDtoV1> getAtonFaultUpdateInfos() {
        // /api/aton/v1/faults
        final DataSourceInfoDtoV1 info = getDataSourceInfo(DataSource.ATON_FAULTS);
        return Collections.singletonList(
            new UpdateInfoDtoV1(ApiConstants.API_ATON_V1_FAULTS,
                                updatedTimestampRepository.getAtonVaultsLastModified(),
                                updatedTimestampRepository.findLastUpdatedInstant(UpdatedTimestampRepository.UpdatedName.ATON_FAULTS_CHECK),
                                info.getUpdateInterval(), info.getRecommendedFetchInterval())
        );
    }

    @Transactional(readOnly = true)
    public DataSourceInfoDtoV1 getDataSourceInfo(final DataSource dataSource) {
        return updatedTimestampRepository.getDataSourceInfo(dataSource);
    }

    @Transactional(readOnly = true)
    public String getDataSourceUpdateInterval(final DataSource dataSource) {
        return updatedTimestampRepository.getDataSourceUpdateInterval(dataSource);
    }

}
