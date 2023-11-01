package fi.livi.digitraffic.meri.dto.geojson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON object", discriminatorProperty = "type")
public abstract class GeoJsonObject implements Serializable {

    private final Map<String, Object> additionalProperties = new HashMap<>();

    public GeoJsonObject() {
    }

    @Schema(description = "GeoJSON object type")
    public abstract String getType();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
