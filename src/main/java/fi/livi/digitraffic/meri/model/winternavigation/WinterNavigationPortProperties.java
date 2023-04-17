package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name", "nationality", "seaArea", "portRestrictions" })
public class WinterNavigationPortProperties extends Properties {

    @Schema(description = "Port SafeSeaNet location code", required = true)
    public final String locode;

    @Schema(description = "Name", required = true)
    public final String name;

    @Schema(description = "Port nationality", required = true)
    public final String nationality;

    @Schema(description = "Sea area where port is located", required = true)
    public final String seaArea;

    @Schema(description = "Currently effective or announced traffic restrictions at the port")
    public final List<PortRestrictionProperty> portRestrictions;

    public WinterNavigationPortProperties(final String locode, final String name, final String nationality, final String seaArea,
                                          final List<PortRestrictionProperty> portRestrictions, final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.locode = locode;
        this.name = name;
        this.nationality = nationality;
        this.seaArea = seaArea;
        this.portRestrictions = portRestrictions;
    }
}
