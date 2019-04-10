package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.annotations.ApiModel;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@ApiModel(description = "GeoJSON FeatureCollection object")
public class PortAreaFeatureCollection extends FeatureCollection<PortAreaFeature> {

    public PortAreaFeatureCollection(final List<PortAreaFeature> features) {
        super(features);
    }
}
