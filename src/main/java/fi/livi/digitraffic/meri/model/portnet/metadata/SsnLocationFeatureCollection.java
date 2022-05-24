package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@Schema(description = "GeoJSON FeatureCollection object")
public class SsnLocationFeatureCollection extends FeatureCollection<SsnLocationFeature> {

    public SsnLocationFeatureCollection(final List<SsnLocationFeature> ssnLocationFeatures) {
        super(ssnLocationFeatures);
    }
}