
package fi.livi.digitraffic.meri.model.pooki;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import org.apache.commons.lang3.builder.ToStringBuilder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON FeatureCollection object")
public class PookiFeatureCollection extends FeatureCollection<PookiFeature> {

    public PookiFeatureCollection() {
        super(null);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }
}
