package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "vesselId",
                     "type",
                     "geometry",
                     "properties" })
@ApiModel(description = "GeoJSON Feature object")
public class WinterNavigationShipFeature extends Feature<Point, WinterNavigationShipProperties> {

    @ApiModelProperty(value = "Vessel identification code. Equals IMO-{IMO-code} when vessel IMO is present. " +
                              "Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
    public final String vesselId;

    public WinterNavigationShipFeature(final String vesselId, final WinterNavigationShipProperties properties, final Point geometry) {
        super(geometry, properties);
        this.vesselId = vesselId;
    }
}
