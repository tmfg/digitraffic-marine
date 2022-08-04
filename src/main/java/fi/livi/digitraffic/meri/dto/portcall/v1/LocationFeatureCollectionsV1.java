package fi.livi.digitraffic.meri.dto.portcall.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationFeatureCollection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@JsonPropertyOrder({
    "dataUpdatedTime",
    "ssnLocationFeatureCollection",
    "portAreaFeatureCollection",
    "berthFeature",
})
public class LocationFeatureCollectionsV1 {
    @Schema(description = "Data last updated", required = true)
    public final Instant dataUpdatedTime;

    @Schema(description = "Ssn Locations in Feature Collection", required = true)
    public final SsnLocationFeatureCollection ssnLocationFeatureCollection;

    @Schema(description = "Port Areas in Feature Collection", required = false)
    public final PortAreaFeatureCollection portAreaFeatureCollection;

    @Schema(description = "Berths in Feature Collection", required = false)
    public final BerthFeatureCollection berthFeatureCollection;

    public LocationFeatureCollectionsV1(final Instant dataUpdatedTime,
                                        final SsnLocationFeatureCollection ssnLocationFeatureCollection,
                                        final PortAreaFeatureCollection portAreaFeatureCollection,
                                        final BerthFeatureCollection berthFeatureCollection) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocationFeatureCollection = ssnLocationFeatureCollection;
        this.portAreaFeatureCollection = portAreaFeatureCollection;
        this.berthFeatureCollection = berthFeatureCollection;
    }

}
