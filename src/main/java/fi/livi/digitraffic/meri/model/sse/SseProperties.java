package fi.livi.digitraffic.meri.model.sse;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class SseProperties {

    @ApiModelProperty(value = "Site name", required = true)
    private final String siteName;

    /* SSE fields */
    @ApiModelProperty(value = "Data last updated", required = true)
    private ZonedDateTime lastUpdate;

    @ApiModelProperty(value = "TODO", required = true)
    private SeaState seaState;

    @ApiModelProperty(value = "TODO", required = true)
    private Trend trend;

    @ApiModelProperty(value = "TODO", required = true)
    private Integer windWaveDir;

    @ApiModelProperty(value = "TODO", required = true)
    private Confidence confidence;

    /* Extra fields / metadata */

    @ApiModelProperty(value = "TODO")
    private Double heelAngle;

    @ApiModelProperty(value = "TODO")
    private LightStatus lightStatus;

    @ApiModelProperty(value = "TODO")
    private Integer temperature;

    private final Map<String, Object> additionalProperties = new HashMap<>();

    public SseProperties(String siteName, ZonedDateTime lastUpdate, SeaState seaState, Trend trend, Integer windWaveDir,
                         Confidence confidence, Double heelAngle, LightStatus lightStatus, Integer temperature) {
        this.siteName = siteName;
        this.lastUpdate = lastUpdate;
        this.seaState = seaState;
        this.trend = trend;
        this.windWaveDir = windWaveDir;
        this.confidence = confidence;
        this.heelAngle = heelAngle;
        this.lightStatus = lightStatus;
        this.temperature = temperature;
    }

    public String getSiteName() {
        return siteName;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public SeaState getSeaState() {
        return seaState;
    }

    public Trend getTrend() {
        return trend;
    }

    public Integer getWindWaveDir() {
        return windWaveDir;
    }

    public Confidence getConfidence() {
        return confidence;
    }

    public Double getHeelAngle() {
        return heelAngle;
    }

    public LightStatus getLightStatus() {
        return lightStatus;
    }

    public Integer getTemperature() {
        return temperature;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void addAdditionalProperties(final Map<String, Object> additionalProperties) {
        additionalProperties.entrySet().forEach(e -> setAdditionalProperty(e.getKey(), e.getValue()));
    }

    /*
     * Enum declarations
     */

    @ApiModel
    public enum Confidence {

        POOR,
        MODERATE,
        GOOD;

        public static Confidence fromValue(final Enum confidence) {
            if (confidence != null) {
                return fromValue(confidence.name());
            }
            return null;
        }

        @JsonCreator
        public static Confidence fromValue(final String value) {
            if (value != null) {
                Confidence.valueOf(value.toUpperCase());
            }
            return null;
        }

    }

    @ApiModel
    public enum SeaState {

        CALM,
        LIGHT,
        MODERATE,
        BREEZE,
        GALE,
        STORM;

        public static SeaState fromValue(final Enum seaState) {
            if (seaState != null) {
                return fromValue(seaState.name());
            }
            return null;
        }

        @JsonCreator
        public static SeaState fromValue(final String value) {
            if (value != null) {
                SeaState.valueOf(value.toUpperCase());
            }
            return null;
        }
    }

    @ApiModel
    public enum Trend {

        UNKNOWN,
        NO_CHANGE,
        ASCENDING,
        DESCENDING;

        public static Trend fromValue(final Enum trend) {
            if (trend != null) {
                return fromValue(trend.name());
            }
            return null;
        }

        @JsonCreator
        public static Trend fromValue(String value) {
            if (value != null) {
                return Trend.valueOf(value.toUpperCase());
            }
            return null;
        }

    }

    @ApiModel
    public enum LightStatus {

        ON,
        OFF,
        ON_D;

        public static LightStatus fromValue(final Enum lightStatus) {
            if (lightStatus != null) {
                return fromValue(lightStatus.name());
            }
            return null;
        }

        @JsonCreator
        public static LightStatus fromValue(String value) {
            if (value != null) {
                LightStatus.valueOf(value.toUpperCase());
            }
            return null;
        }

    }
}
