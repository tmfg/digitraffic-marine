package fi.livi.digitraffic.meri.model.winternavigation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "name", "nationality", "seaArea", "portRestrictions" })
public class WinterNavigationPortProperties extends Properties {

    @ApiModelProperty(value = "Name", required = true)
    public final String name;

    @ApiModelProperty(value = "Port nationality", required = true)
    public final String nationality;

    @ApiModelProperty(value = "Sea area where port is located", required = true)
    public final String seaArea;

    @ApiModelProperty(value = "Currently effective or announced traffic restrictions at the port")
    public final List<PortRestrictionProperty> portRestrictions;

    public WinterNavigationPortProperties(final String name, final String nationality, final String seaArea,
                                          final List<PortRestrictionProperty> portRestrictions) {
        this.name = name;
        this.nationality = nationality;
        this.seaArea = seaArea;
        this.portRestrictions = portRestrictions;
    }
}
