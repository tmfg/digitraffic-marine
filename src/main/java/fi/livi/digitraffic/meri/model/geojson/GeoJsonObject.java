package fi.livi.digitraffic.meri.model.geojson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "type", "id"})
public abstract class GeoJsonObject implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ApiModelProperty(required = true, value = "Type of GeoJSON object")
    @JsonProperty(value = "type", required = true)
    private String type;

    private Map<String, Object> additionalProperties = new HashMap<>();

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        log.warn("Unmapped field: " + name + " = " + value);
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
