package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name", "nationality", "seaArea", "portRestrictions" })
public class WinterNavigationPortPropertiesV1 extends Properties {

    @Schema(description = "Port SafeSeaNet location code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String name;

    @Schema(description = "Port nationality", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String nationality;

    @Schema(description = "Sea area where port is located", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String seaArea;

    @Schema(description = "Currently effective or announced traffic restrictions at the port")
    public final List<WinterNavigationPortRestrictionV1> portRestrictions;

    public WinterNavigationPortPropertiesV1(final String locode, final String name, final String nationality, final String seaArea,
                                            final List<WinterNavigationPortRestrictionV1> portRestrictions, final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.locode = locode;
        this.name = name;
        this.nationality = nationality;
        this.seaArea = seaArea;
        this.portRestrictions = portRestrictions;
    }
}
