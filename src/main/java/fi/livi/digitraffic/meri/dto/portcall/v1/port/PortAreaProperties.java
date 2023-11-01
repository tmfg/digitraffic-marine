package fi.livi.digitraffic.meri.dto.portcall.v1.port;

import java.time.Instant;

import fi.livi.digitraffic.meri.dto.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port area GeoJSON Properties")
public class PortAreaProperties extends Properties {
    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Port area name", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String portAreaName;

    public PortAreaProperties(final String locode, final String portAreaName, final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.locode = locode;
        this.portAreaName = portAreaName;
    }
}
