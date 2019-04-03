
package fi.livi.digitraffic.meri.domain.sse.tlsc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.util.StringUtil;

/**
 * The Extra_fields Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "temperature",
    "batteryVoltage",
    "additionalProperties"
})
public class ExtraFields {

    private Integer temperature;
    private Double batteryVoltage;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ExtraFields(final Integer temperature, final Double battVoltage, final Map<String, Object> additionalProperties) {
        this(temperature, battVoltage);
        this.additionalProperties = additionalProperties;
    }

    @JsonCreator
    public ExtraFields(final Integer temperature, final Double battVoltage) {
        this.temperature = temperature;
        this.batteryVoltage = battVoltage;
    }

    public Integer getTemperature() {
        return temperature;
    }
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    public Double getBatteryVoltage() {
        return batteryVoltage;
    }
    public void setBatteryVoltage(Double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
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
