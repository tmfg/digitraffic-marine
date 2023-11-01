package fi.livi.digitraffic.meri.dto.portcall.v1.port;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.FeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@Schema(description = "Ports GeoJSON feature collection")
public class PortFeatureCollection extends FeatureCollection<PortFeature>  {

    public PortFeatureCollection(final List<PortFeature> portFeatures, final Instant dataUpdatedTime) {
        super(portFeatures, dataUpdatedTime);
    }
}