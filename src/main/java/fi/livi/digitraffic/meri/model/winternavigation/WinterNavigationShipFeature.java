package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "vesselId",
                     "type",
                     "geometry",
                     "properties" })
public class WinterNavigationShipFeature extends Feature<Point, WinterNavigationShipProperties> {

    @Schema(description = "Vessel identification code. Equals IMO-{IMO-code} when vessel IMO is present. " +
                              "Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
    public final String vesselId;

    public WinterNavigationShipFeature(final String vesselId, final WinterNavigationShipProperties properties, final Point geometry) {
        super(geometry, properties);
        this.vesselId = vesselId;
    }
}
