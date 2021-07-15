package fi.livi.digitraffic.meri.model.sse;

import java.math.BigDecimal;
import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import fi.livi.digitraffic.meri.util.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class SseProperties extends Properties {

    // For fixed AtoNs, only the light status, last update, confidence and temperature fields are usable.
    private static final String FIELD_ONLY_FOR_FLOATING_SITE = "This field is available only for FLOATING siteType";

    @ApiModelProperty(value = "Identifier of the site", position = 1)
    public final int siteNumber;

    @ApiModelProperty(value = "Site name of the buoy", required = true)
    public final String siteName;

    @ApiModelProperty(value = "Type of the site. FLOATING is floating in the water and FIXED it is standing on the ground.", required = true)
    private SiteType siteType;

    /* SSE fields */
    @ApiModelProperty(value = "Data last updated timestamp in ISO 8601 format with time offsets from UTC (eg. 2016-04-20T12:38:16.328+03:00 or 2018-11-09T09:41:09Z)", required = true)
    private Instant lastUpdate;

    @ApiModelProperty(value = "Sea state. If seaState is CALM, the windWaveDir is not reliable. " + FIELD_ONLY_FOR_FLOATING_SITE)
    private SeaState seaState;

    @ApiModelProperty(value = "Trend of condition change. " + FIELD_ONLY_FOR_FLOATING_SITE)
    private Trend trend;

    @ApiModelProperty(value = "Wind and wave direction in degrees from north. If seaState is CALM, the windWaveDir is not reliable. " + FIELD_ONLY_FOR_FLOATING_SITE, allowableValues = "range[0, 359]")
    private Integer windWaveDir;

    @ApiModelProperty(value =
        "Provides information on estimated confidence of mainly the directional information based on the Horizontal Dilution of precision (HDOP) figure obtained from the GPS receiver:\n"+
        "* POOR: More than 10\n" +
        "* MODERATE: 2-10\n" +
        "* GOOD: Less than 2", required = true)
    private Confidence confidence;

    /* Extra fields / metadata */

    @ApiModelProperty(value = "Heel angle of the buoy in degrees (°), 0 is upright and it´s unsigned. " + FIELD_ONLY_FOR_FLOATING_SITE, allowableValues = "range[0, 90]")
    private BigDecimal heelAngle;

    @ApiModelProperty(value = "Status of the flashlight. Normally ON at nighttime and OFF at daytime. " +
                              "ON_D means that light characteristics are alternated (ie. intensity and frequency) " +
                              "compared to normal ON operation, used normally on daytime.")
    private LightStatus lightStatus;

    @ApiModelProperty(value = "Temperature of the air in celsius degree (°C). Since the sensor is inside of the buoy and sunlight can " +
                              "heat up the enclosure this can be used only as a reference measurement as it can " +
                              "show higher readings than the actual air temperature")
    private Integer temperature;

    @JsonIgnore // For internal use
    private Instant created;

    public SseProperties(final int siteNumber, final String siteName, final SiteType siteType, final Instant lastUpdate, final SeaState seaState, final Trend trend, final Integer windWaveDir,
                         final Confidence confidence, final BigDecimal heelAngle, final LightStatus lightStatus, final Integer temperature, final Instant created) {
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
        this.created = TimeUtil.withoutMillis(created);
    }

    public int getSiteNumber() {
        return siteNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public Instant getLastUpdate() {
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

    public BigDecimal getHeelAngle() {
        return heelAngle;
    }

    public LightStatus getLightStatus() {
        return lightStatus;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Instant getCreated() {
        return created;
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
        public static Trend fromValue(final String value) {
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
        public static LightStatus fromValue(final String value) {
            if (value != null) {
                return LightStatus.valueOf(value.toUpperCase());
            }
            return null;
        }
    }

    @ApiModel
    public enum SiteType {

        FIXED,
        FLOATING;

        public static SiteType fromValue(final Enum siteType) {
            if (siteType != null) {
                return fromValue(siteType.name());
            }
            return null;
        }

        @JsonCreator
        public static SiteType fromValue(final String value) {
            if (value != null) {
                return SiteType.valueOf(value.toUpperCase());
            }
            return null;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SseProperties that = (SseProperties) o;

        return new EqualsBuilder().append(siteNumber, that.siteNumber).append(siteName, that.siteName)
            .append(siteType, that.siteType).append(lastUpdate, that.lastUpdate).append(seaState, that.seaState).append(trend, that.trend)
            .append(windWaveDir, that.windWaveDir).append(confidence, that.confidence).append(heelAngle, that.heelAngle)
            .append(lightStatus, that.lightStatus).append(temperature, that.temperature).append(created, that.created).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(siteNumber).append(siteName).append(siteType).append(lastUpdate).append(seaState).append(trend)
            .append(windWaveDir).append(confidence).append(heelAngle).append(lightStatus).append(temperature).append(created).toHashCode();
    }
}
