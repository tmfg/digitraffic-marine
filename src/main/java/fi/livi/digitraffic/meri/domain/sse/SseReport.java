package fi.livi.digitraffic.meri.domain.sse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fi.livi.digitraffic.meri.model.sse.SseProperties;

@Entity
@DynamicInsert
public class SseReport {

    @Id
    @GenericGenerator(name = "SEQ_SSE_REPORT", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                      parameters = @Parameter(name = "sequence_name", value = "SEQ_SSE_REPORT"))
    @GeneratedValue(generator = "SEQ_SSE_REPORT")
    private Long sseReportId;
    private ZonedDateTime created;
    private Boolean latest;

    // SSE data
    private Integer siteNumber;
    private String siteName;
    private ZonedDateTime lastUpdate;
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

    public SseReport(final ZonedDateTime created, final Boolean latest, final Integer siteNumber, final String siteName, final ZonedDateTime lastUpdate,
                     final SseProperties.SeaState seaState, final SseProperties.Trend trend, final Integer windWaveDir,
                     final SseProperties.Confidence confidence, final BigDecimal heelAngle, final SseProperties.LightStatus lightStatus, final Integer temperature,
                     final BigDecimal longitude, final BigDecimal latitude) {
        this.created = created;
        this.latest = latest;
        this.siteNumber = siteNumber;
        this.siteName = siteName;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
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

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
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
}
