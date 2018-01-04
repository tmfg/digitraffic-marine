package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Geometry;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "vesselId",
                     "type",
                     "geometry",
                     "properties" })
@ApiModel(description = "GeoJSON Feature object")
public class WinterNavigationShipFeature {

    @ApiModelProperty(value = "Vessel identification code. Equals IMO-<IMO-code> when vessel IMO is present. " +
                              "Otherwise MMSI-<MMSI-code> (Maritime Mobile Service Identity).", required = true)
    public final String vesselId;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final WinterNavigationShipProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public WinterNavigationShipFeature(final String vesselId, final WinterNavigationShipProperties properties, final Point geometry) {
        this.vesselId = vesselId;
        this.properties = properties;
        this.geometry = geometry;
    }
}
