package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@Schema(description = "Port area GeoJSON feature collection")
public class PortAreaFeatureCollection extends FeatureCollection<PortAreaFeature> {

    public PortAreaFeatureCollection(final List<PortAreaFeature> features, final Instant dataUpdatedTime) {
        super(features, dataUpdatedTime);
    }
}
