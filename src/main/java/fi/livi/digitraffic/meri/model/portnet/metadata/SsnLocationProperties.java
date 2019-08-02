package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Properties.class)
public class SsnLocationProperties extends Properties {
    @ApiModelProperty(value = "Location name", required = true)
    public final String locationName;

    @ApiModelProperty(value = "Country", required = true)
    public final String country;

    public SsnLocationProperties(final String locationName, final String country) {
        this.locationName = locationName;
        this.country = country;
    }
}
