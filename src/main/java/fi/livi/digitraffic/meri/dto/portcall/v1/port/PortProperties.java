package fi.livi.digitraffic.meri.dto.portcall.v1.port;

import java.time.Instant;

import fi.livi.digitraffic.meri.dto.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port GeoJSON Properties")
public class PortProperties extends Properties {

    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Location name", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locationName;

    @Schema(description = "Country", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String country;

    public PortProperties(final String locode, final String locationName, final String country, final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.locode = locode;
        this.locationName = locationName;
        this.country = country;
    }
}
