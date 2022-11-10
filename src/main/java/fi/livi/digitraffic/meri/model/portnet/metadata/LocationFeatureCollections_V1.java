package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "dataUpdatedTime",
        "ssnLocationFeatureCollection",
        "portAreaFeatureCollection",
        "berthFeature",
})
@Schema(description = "SafeSeaNet (SSN) GeoJSON feature collection")
public class LocationFeatureCollections_V1 {
    @Schema(description = "Data last updated", required = true)
    public final Instant dataUpdatedTime;

    @Schema(description = "Ssn Locations in Feature Collection", required = true)
    public final PortFeatureCollection ssnLocationFeatureCollection;

    @Schema(description = "Port Areas in Feature Collection")
    public final PortAreaFeatureCollection portAreaFeatureCollection;

    @Schema(description = "Berths in Feature Collection")
    public final BerthFeatureCollection berthFeatureCollection;

    public LocationFeatureCollections_V1(final Instant dataUpdatedTime,
                                         final PortFeatureCollection ssnLocationFeatureCollection,
                                         final PortAreaFeatureCollection portAreaFeatureCollection,
                                         final BerthFeatureCollection berthFeatureCollection) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocationFeatureCollection = ssnLocationFeatureCollection;
        this.portAreaFeatureCollection = portAreaFeatureCollection;
        this.berthFeatureCollection = berthFeatureCollection;
    }
}
