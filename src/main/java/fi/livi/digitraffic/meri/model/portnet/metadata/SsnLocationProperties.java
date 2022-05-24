package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

public class SsnLocationProperties extends Properties {

    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @Schema(description = "Location name", required = true)
    public final String locationName;

    @Schema(description = "Country", required = true)
    public final String country;

    public SsnLocationProperties(final String locode, final String locationName, final String country) {
        this.locode = locode;
        this.locationName = locationName;
        this.country = country;
    }
}
