package fi.livi.digitraffic.meri.model.ais;

import java.util.List;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON FeatureCollection object")
public class VesselLocationFeatureCollection extends FeatureCollection<VesselLocationFeature> {

    public VesselLocationFeatureCollection(final List<VesselLocationFeature> features) {
        super(features);
    }
}
