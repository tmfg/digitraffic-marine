
package fi.livi.digitraffic.meri.model.pooki;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.GeoJsonObject;
import fi.livi.digitraffic.meri.model.geojson.Geometry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
    "type",
    "geometry",
    "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class PookiFeature extends GeoJsonObject {

    @ApiModelProperty("GeoJSON Properties object")
    @JsonProperty("properties")
    private PookiProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    @JsonProperty("geometry")
    private Geometry geometry;

    public void setProperties(PookiProperties properties) {
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
