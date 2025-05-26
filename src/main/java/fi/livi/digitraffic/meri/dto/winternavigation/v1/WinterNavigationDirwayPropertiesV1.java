package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "issueTime", "issuerCode", "issuerName", "validUntil" })
public class WinterNavigationDirwayPropertiesV1 extends Properties {

    @Schema(description = "Name of the dirway", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String name;

    @Schema(description = "Time when the dirway was issued")
    public final Instant issueTime;

    @Schema(description = "Issuer code, e.g. URH")
    public final String issuerCode;

    @Schema(description = "Issuer name, e.g. URHO")
    public final String issuerName;

    @Schema(description = "Date and time until this dirway was in effect")
    public final Instant validUntil;

    public WinterNavigationDirwayPropertiesV1(final String name,
                                              final Instant issueTime,
                                              final String issuerCode,
                                              final String issuerName,
                                              final Instant validUntil,
                                              final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.name = name;
        this.issueTime = issueTime;
        this.issuerCode = issuerCode;
        this.issuerName = issuerName;
        this.validUntil = validUntil;
    }
}
