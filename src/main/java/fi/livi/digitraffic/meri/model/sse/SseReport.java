package fi.livi.digitraffic.meri.model.sse;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.DynamicInsert;

import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1.SiteType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@DynamicInsert
public class SseReport {

    @Id
    @SequenceGenerator(name = "SEQ_SSE_REPORT", sequenceName = "SEQ_SSE_REPORT", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_SSE_REPORT")
    private Long sseReportId;
    private Instant created;
    private Boolean latest;

    // SSE data
    private Integer siteNumber;
    private String siteName;
    @Enumerated(EnumType.STRING)
    private SiteType siteType;
    private Instant lastUpdate;
    @Enumerated(EnumType.STRING)
    private SsePropertiesV1.SeaState seaState;
    @Enumerated(EnumType.STRING)
    private SsePropertiesV1.Trend trend;
    private Integer windWaveDir;
    @Enumerated(EnumType.STRING)
    private SsePropertiesV1.Confidence confidence;
    // Extra fields / metadata
    private BigDecimal heelAngle;
    @Enumerated(EnumType.STRING)
    private SsePropertiesV1.LightStatus lightStatus;
    private Integer temperature;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public SseReport() {}

    public SseReport(final Instant created, final Boolean latest, final Integer siteNumber, final String siteName, final SiteType siteType, final Instant lastUpdate,
                     final SsePropertiesV1.SeaState seaState, final SsePropertiesV1.Trend trend, final Integer windWaveDir,
                     final SsePropertiesV1.Confidence confidence, final BigDecimal heelAngle, final SsePropertiesV1.LightStatus lightStatus, final Integer temperature,
                     final BigDecimal longitude, final BigDecimal latitude) {
        this.created = created;
        this.latest = latest;
        this.siteNumber = siteNumber;
        this.siteName = siteName;
        this.siteType = siteType;
        this.lastUpdate = lastUpdate;
        this.seaState = seaState;
        this.trend = trend;
        this.windWaveDir = windWaveDir;
        this.confidence = confidence;
        this.heelAngle = heelAngle;
        this.lightStatus = lightStatus;
        this.temperature = temperature;
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public Long getSseReportId() {
        return sseReportId;
    }

    public void setSseReportId(final Long sseReportId) {
        this.sseReportId = sseReportId;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(final Instant created) {
        this.created = created;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(final Boolean latest) {
        this.latest = latest;
    }

    public Integer getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(final Integer siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(final SiteType siteType) {
        this.siteType = siteType;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(final Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SsePropertiesV1.SeaState getSeaState() {
        return seaState;
    }

    public void setSeaState(final SsePropertiesV1.SeaState seaState) {
        this.seaState = seaState;
    }

    public SsePropertiesV1.Trend getTrend() {
        return trend;
    }

    public void setTrend(final SsePropertiesV1.Trend trend) {
        this.trend = trend;
    }

    public Integer getWindWaveDir() {
        return windWaveDir;
    }

    public void setWindWaveDir(final Integer windWaveDir) {
        this.windWaveDir = windWaveDir;
    }

    public SsePropertiesV1.Confidence getConfidence() {
        return confidence;
    }

    public void setConfidence(final SsePropertiesV1.Confidence confidence) {
        this.confidence = confidence;
    }

    public BigDecimal getHeelAngle() {
        return heelAngle;
    }

    public void setHeelAngle(final BigDecimal heelAngle) {
        this.heelAngle = heelAngle;
    }

    public SsePropertiesV1.LightStatus getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(final SsePropertiesV1.LightStatus lightStatus) {
        this.lightStatus = lightStatus;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(final Integer temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    public boolean isFloating() {
        return SiteType.FLOATING.equals(getSiteType());
    }
}
