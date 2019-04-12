
package fi.livi.digitraffic.meri.model.sse.tlsc;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.LightStatus;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.livi.digitraffic.meri.util.StringUtil;

public class SseExtraFields {

    /** WGS84 coordinates in decimal degrees. */
    private Double coordLatitude;
    /** WGS84 coordinates in decimal degrees. */
    private Double coordLongitude;
    private Double heelAngle;
    private LightStatus lightStatus;
    private Integer temperature;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public SseExtraFields() {
    }

    @JsonCreator
    public SseExtraFields(final Double coordLatitude, final Double coordLongitude, final Double heelAngle,
                          final LightStatus lightStatus, final Integer temperature) {
        this.coordLatitude = coordLatitude;
        this.coordLongitude = coordLongitude;
        this.heelAngle = heelAngle;
        this.lightStatus = lightStatus;
        this.temperature = temperature;
    }

    public SseExtraFields(final Double coordLatitude, final Double coordLongitude, final Double heelAngle, final LightStatus lightStatus,
                          final Integer temperature, final Map<String, Object> additionalProperties) {
        this(coordLatitude, coordLongitude, heelAngle, lightStatus, temperature);
        this.additionalProperties = additionalProperties;
    }

    public Double getCoordLatitude() {
        return coordLatitude;
    }

    public void setCoordLatitude(Double coordLatitude) {
        this.coordLatitude = coordLatitude;
    }

    public Double getCoordLongitude() {
        return coordLongitude;
    }

    public void setCoordLongitude(Double coordLongitude) {
        this.coordLongitude = coordLongitude;
    }

    public Double getHeelAngle() {
        return heelAngle;
    }

    public void setHeelAngle(Double heelAngle) {
        this.heelAngle = heelAngle;
    }

    public LightStatus getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(LightStatus lightStatus) {
        this.lightStatus = lightStatus;
    }

    public Integer getTemperature() {
        return temperature;
    }
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return StringUtil.toJsonString(this);
    }
}
