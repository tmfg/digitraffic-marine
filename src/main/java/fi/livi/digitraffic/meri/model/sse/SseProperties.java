package fi.livi.digitraffic.meri.model.sse;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON properties of SseFeature")
public class SseProperties {

    @ApiModelProperty(value = "Site name of the buoy", required = true)
    public final String siteName;

    /* SSE fields */
    @ApiModelProperty(value = "Data last updated timestamp in ISO 8601 format with time offsets from UTC (eg. 2016-04-20T12:38:16.328+03:00 or 2018-11-09T09:41:09Z)", required = true)
    private ZonedDateTime lastUpdate;

    @ApiModelProperty(value = "Sea state", required = true)
    private SeaState seaState;

    @ApiModelProperty(value = "Trend of condition change", required = true)
    private Trend trend;

    @ApiModelProperty(value = "Wind and wave direction in degrees from north", allowableValues = "range[0, 359]", required = true)
    private Integer windWaveDir;

    @ApiModelProperty(value = "Reliability of the SSE data", required = true)
    private Confidence confidence;

    /* Extra fields / metadata */

    @ApiModelProperty(value = "Heel angle of the buoy in degrees (°), 0 is upright and it´s unsigned", allowableValues = "range[0, 90]")
    private Double heelAngle;

    @ApiModelProperty(value = "Status of the flashlight. Normally ON at nighttime and OFF at daytime. " +
                              "ON_D means that light characteristics are alternated (ie. intensity and frequency) " +
                              "compared to normal ON operation, used normally on daytime.")
    private LightStatus lightStatus;

    @ApiModelProperty(value = "Temperature of the air in celsius degree (°C). Since the sensor is inside of the buoy and sunlight can " +
                              "heat up the enclosure this can be used only as a reference measurement as it can " +
                              "show higher readings than the actual air temperature")
    private Integer temperature;

    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
                return Confidence.valueOf(value.toUpperCase());
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
                return SeaState.valueOf(value.toUpperCase());
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
        ON_D; // Light flashing with alternative flash characteristics, normally on daytime

        public static LightStatus fromValue(final Enum lightStatus) {
            if (lightStatus != null) {
                return fromValue(lightStatus.name());
            }
            return null;
        }

        @JsonCreator
        public static LightStatus fromValue(String value) {
            if (value != null) {
                return LightStatus.valueOf(value.toUpperCase());
            }
            return null;
        }

    }
}
