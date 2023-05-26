package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Berth properties")
public class BerthProperties extends Properties {
    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Port area code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String portAreaCode;

    @Schema(description = "Berth code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String berthCode;

    @Schema(description = "Berth name", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String berthName;

    public BerthProperties(final String locode, final String portAreaCode, final String berthCode, final String berthName, final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.berthCode = berthCode;
        this.berthName = berthName;
    }
}
