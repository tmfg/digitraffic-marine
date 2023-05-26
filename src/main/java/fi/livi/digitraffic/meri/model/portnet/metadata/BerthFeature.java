package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@Schema(description = "Berth GeoJSON feature")
public class BerthFeature extends Feature<Point, BerthProperties> {

    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Port area code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String portAreaCode;

    @Schema(description = "Berth code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String berthCode;

    public BerthFeature(final String locode, final String portAreaCode, final String berthCode, final BerthProperties properties) {
        super(null, properties);
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.berthCode = berthCode;
    }
}
