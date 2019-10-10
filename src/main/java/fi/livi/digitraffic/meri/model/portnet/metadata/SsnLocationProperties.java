package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

public class SsnLocationProperties extends Properties {

    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true, position = 1)
    public final String locode;

    @ApiModelProperty(value = "Location name", required = true)
    public final String locationName;

    @ApiModelProperty(value = "Country", required = true)
    public final String country;

    public SsnLocationProperties(final String locode, final String locationName, final String country) {
        this.locode = locode;
        this.locationName = locationName;
        this.country = country;
    }
}
