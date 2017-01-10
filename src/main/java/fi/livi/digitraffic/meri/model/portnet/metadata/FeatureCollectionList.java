package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "dataUpdatedTime",
        "ssnLocationFeatureCollection",
        "portAreaFeatureCollection",
        "berthFeature",
})
public class FeatureCollectionList {
    @ApiModelProperty(value = "Data last updated", required = true)
    public final ZonedDateTime dataUpdatedTime;

    @ApiModelProperty(value = "Ssn Locations in Feature Collection", required = true)
    public final SsnLocationFeatureCollection ssnLocationFeatureCollection;

    @ApiModelProperty(value = "Port Areas in Feature Collection", required = false)
    public final PortAreaFeatureCollection portAreaFeatureCollection;

    @ApiModelProperty(value = "Berths in Feature Collection", required = false)
    public final BerthFeatureCollection berthFeatureCollection;

    public FeatureCollectionList(final ZonedDateTime dataUpdatedTime,
                                 final SsnLocationFeatureCollection ssnLocationFeatureCollection,
                                 final PortAreaFeatureCollection portAreaFeatureCollection,
                                 final BerthFeatureCollection berthFeatureCollection) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocationFeatureCollection = ssnLocationFeatureCollection;
        this.portAreaFeatureCollection = portAreaFeatureCollection;
        this.berthFeatureCollection = berthFeatureCollection;
    }
}
