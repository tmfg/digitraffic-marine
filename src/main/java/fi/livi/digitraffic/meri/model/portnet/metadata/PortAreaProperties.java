package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port area GeoJSON Properties")
public class PortAreaProperties extends Properties {
    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @Schema(description = "Port area name", required = true)
    public final String portAreaName;

    public PortAreaProperties(final String locode, final String portAreaName) {
        this.locode = locode;
        this.portAreaName = portAreaName;
    }
}
