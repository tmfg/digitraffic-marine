package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port GeoJSON Properties")
public class PortProperties extends Properties {

    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @Schema(description = "Location name", required = true)
    public final String locationName;

    @Schema(description = "Country", required = true)
    public final String country;

    public PortProperties(final String locode, final String locationName, final String country) {
        this.locode = locode;
        this.locationName = locationName;
        this.country = country;
    }
}
