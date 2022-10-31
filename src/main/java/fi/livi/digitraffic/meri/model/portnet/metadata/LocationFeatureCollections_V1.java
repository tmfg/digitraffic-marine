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
//@Schema(description = "") // TODO
public class LocationFeatureCollections_V1 {
    @Schema(description = "Data last updated", required = true)
    public final ZonedDateTime dataUpdatedTime;

    @Schema(description = "Ssn Locations in Feature Collection", required = true)
    public final SsnLocationFeatureCollection ssnLocationFeatureCollection;

    @Schema(description = "Port Areas in Feature Collection")
    public final PortAreaFeatureCollection portAreaFeatureCollection;

    @Schema(description = "Berths in Feature Collection")
    public final BerthFeatureCollection berthFeatureCollection;

    public LocationFeatureCollections_V1(final ZonedDateTime dataUpdatedTime,
                                         final SsnLocationFeatureCollection ssnLocationFeatureCollection,
                                         final PortAreaFeatureCollection portAreaFeatureCollection,
                                         final BerthFeatureCollection berthFeatureCollection) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocationFeatureCollection = ssnLocationFeatureCollection;
        this.portAreaFeatureCollection = portAreaFeatureCollection;
        this.berthFeatureCollection = berthFeatureCollection;
    }
}
