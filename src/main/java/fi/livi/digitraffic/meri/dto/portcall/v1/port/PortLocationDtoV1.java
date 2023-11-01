package fi.livi.digitraffic.meri.dto.portcall.v1.port;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
    "dataUpdatedTime",
    "ssnLocationFeatureCollection",
    "portAreaFeatureCollection",
    "berthFeature",
})
@Schema(description = "Ports, port areas and berths locations.")
public class PortLocationDtoV1 implements LastModifiedSupport {

    @Schema(description = "Data last updated", requiredMode = Schema.RequiredMode.REQUIRED)
    public final Instant dataUpdatedTime;

    @Schema(description = "Ssn Locations in Feature Collection", requiredMode = Schema.RequiredMode.REQUIRED)
    public final PortFeatureCollection ssnLocations;

    @Schema(description = "Port Areas in Feature Collection")
    public final PortAreaFeatureCollection portAreas;

    @Schema(description = "Berths in berths collection")
    public final BerthCollection berths;

    public PortLocationDtoV1(final Instant dataUpdatedTime,
                             final PortFeatureCollection ssnLocations,
                             final PortAreaFeatureCollection portAreas,
                             final BerthCollection berths) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.ssnLocations = ssnLocations;
        this.portAreas = portAreas;
        this.berths = berths;
    }

    @Override
    public Instant getLastModified() {
        return dataUpdatedTime;
    }
}
