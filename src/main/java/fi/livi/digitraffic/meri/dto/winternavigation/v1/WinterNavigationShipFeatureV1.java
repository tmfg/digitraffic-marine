package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "vesselId",
                     "type",
                     "geometry",
                     "properties" })
public class WinterNavigationShipFeatureV1 extends Feature<Point, WinterNavigationShipPropertiesV1> {

    @Schema(description = "Vessel identification code. Equals IMO-{IMO-code} when vessel IMO is present. " +
                              "Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String vesselId;

    public WinterNavigationShipFeatureV1(final String vesselId, final WinterNavigationShipPropertiesV1 properties, final Point geometry) {
        super(geometry, properties);
        this.vesselId = vesselId;
    }
}
