package fi.livi.digitraffic.meri.domain.sse;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;

import org.hibernate.annotations.DynamicInsert;

import fi.livi.digitraffic.meri.model.sse.SseProperties;
import fi.livi.digitraffic.meri.model.sse.SseProperties.SiteType;

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
    private SseProperties.SeaState seaState;
    @Enumerated(EnumType.STRING)
    private SseProperties.Trend trend;
    private Integer windWaveDir;
    @Enumerated(EnumType.STRING)
    private SseProperties.Confidence confidence;
    // Extra fields / metadata
    private BigDecimal heelAngle;
    @Enumerated(EnumType.STRING)
    private SseProperties.LightStatus lightStatus;
    private Integer temperature;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public SseReport() {}

    public SseReport(final Instant created, final Boolean latest, final Integer siteNumber, final String siteName, final SiteType siteType, final Instant lastUpdate,
                     final SseProperties.SeaState seaState, final SseProperties.Trend trend, final Integer windWaveDir,
                     final SseProperties.Confidence confidence, final BigDecimal heelAngle, final SseProperties.LightStatus lightStatus, final Integer temperature,
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

    public void setSseReportId(Long sseReportId) {
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

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Integer getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(Integer siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SseProperties.SeaState getSeaState() {
        return seaState;
    }

    public void setSeaState(SseProperties.SeaState seaState) {
        this.seaState = seaState;
    }

    public SseProperties.Trend getTrend() {
        return trend;
    }

    public void setTrend(SseProperties.Trend trend) {
        this.trend = trend;
    }

    public Integer getWindWaveDir() {
        return windWaveDir;
    }

    public void setWindWaveDir(Integer windWaveDir) {
        this.windWaveDir = windWaveDir;
    }

    public SseProperties.Confidence getConfidence() {
        return confidence;
    }

    public void setConfidence(SseProperties.Confidence confidence) {
        this.confidence = confidence;
    }

    public BigDecimal getHeelAngle() {
        return heelAngle;
    }

    public void setHeelAngle(final BigDecimal heelAngle) {
        this.heelAngle = heelAngle;
    }

    public SseProperties.LightStatus getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(final SseProperties.LightStatus lightStatus) {
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

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public boolean isFloating() {
        return SiteType.FLOATING.equals(getSiteType());
    }
}
