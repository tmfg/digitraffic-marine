package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.data.v1.DataUpdatedSupportV1;
import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@Schema(description = "Ports GeoJSON feature collection")
public class PortFeatureCollection extends FeatureCollection<PortFeature> implements DataUpdatedSupportV1 {

    public PortFeatureCollection(final List<PortFeature> portFeatures, final Instant dataUpdatedTime) {
        super(portFeatures, dataUpdatedTime);
    }
}