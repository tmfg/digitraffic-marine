
package fi.livi.digitraffic.meri.model.pooki;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Geometry;
import io.swagger.annotations.ApiModel;

@JsonPropertyOrder({
    "type",
    "geometry",
    "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class PookiFeature extends Feature<Geometry, PookiProperties> {

    public PookiFeature() {
    }

    public PookiFeature(PookiProperties properties) {
        super(properties);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}