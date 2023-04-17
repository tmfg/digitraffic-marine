package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "issueTime", "issuerCode", "issuerName", "validUntil" })
public class WinterNavigationDirwayProperties extends Properties {

    @Schema(description = "Name of the dirway", required = true)
    public final String name;

    @Schema(description = "Time when the dirway was issued")
    public final ZonedDateTime issueTime;

    @Schema(description = "Issuer code, e.g. URH")
    public final String issuerCode;

    @Schema(description = "Issuer name, e.g. URHO")
    public final String issuerName;

    @Schema(description = "Date and time until this dirway was in effect")
    public final ZonedDateTime validUntil;

    public WinterNavigationDirwayProperties(final String name,
                                            final ZonedDateTime issueTime,
                                            final String issuerCode,
                                            final String issuerName,
                                            final ZonedDateTime validUntil,
                                            final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.name = name;
        this.issueTime = issueTime;
        this.issuerCode = issuerCode;
        this.issuerName = issuerName;
        this.validUntil = validUntil;
    }
}
