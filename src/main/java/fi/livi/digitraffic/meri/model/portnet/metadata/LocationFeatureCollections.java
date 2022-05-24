package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "dataUpdatedTime",
        "ssnLocationFeatureCollection",
        "portAreaFeatureCollection",
        "berthFeature",
})
public class LocationFeatureCollections {
    @Schema(description = "Data last updated", required = true)
    public final ZonedDateTime dataUpdatedTime;

    @Schema(description = "Ssn Locations in Feature Collection", required = true)
    public final SsnLocationFeatureCollection ssnLocationFeatureCollection;

    @Schema(description = "Port Areas in Feature Collection", required = false)
    public final PortAreaFeatureCollection portAreaFeatureCollection;

    @Schema(description = "Berths in Feature Collection", required = false)
    public final BerthFeatureCollection berthFeatureCollection;

    public LocationFeatureCollections(final ZonedDateTime dataUpdatedTime,
                                 final SsnLocationFeatureCollection ssnLocationFeatureCollection,
                                 final PortAreaFeatureCollection portAreaFeatureCollection,
                                 final BerthFeatureCollection berthFeatureCollection) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocationFeatureCollection = ssnLocationFeatureCollection;
        this.portAreaFeatureCollection = portAreaFeatureCollection;
        this.berthFeatureCollection = berthFeatureCollection;
    }
}
