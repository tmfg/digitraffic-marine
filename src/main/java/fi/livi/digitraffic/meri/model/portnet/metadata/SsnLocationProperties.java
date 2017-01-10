package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SsnLocationProperties {
    @ApiModelProperty(value = "Location name", required = true)
    public final String locationName;

    @ApiModelProperty(value = "Country", required = true)
    public final String country;

    public SsnLocationProperties(final String locationName, final String country) {
        this.locationName = locationName;
        this.country = country;
    }
}
