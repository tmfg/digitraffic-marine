package fi.livi.digitraffic.meri.dto.ais.v1;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.dto.geojson.FeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON FeatureCollection object")
public class VesselLocationFeatureCollectionV1 extends FeatureCollection<VesselLocationFeatureV1> {

    public VesselLocationFeatureCollectionV1(final List<VesselLocationFeatureV1> features, final Instant dataUpdatedTime) {
        super(features, dataUpdatedTime);
    }
}
