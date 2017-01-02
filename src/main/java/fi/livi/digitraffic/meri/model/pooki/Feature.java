
package fi.livi.digitraffic.meri.model.pooki;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "geometry",
    "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class Feature extends GeoJsonObject {

    @ApiModelProperty("GeoJSON Properties object")
    @JsonProperty("properties")
    private Properties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    @JsonProperty("geometry")
    private Geometry geometry;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @ApiModelProperty(allowableValues = "Feature", required = true)
    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
