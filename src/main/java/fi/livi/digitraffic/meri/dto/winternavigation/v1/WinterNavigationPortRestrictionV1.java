package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.ZonedDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port restriction")
public class WinterNavigationPortRestrictionV1 {

    @Schema(description = "Indicates whether this restriction is currently in effect (true); false implies past/future restriction")
    public final Boolean isCurrent;

    @Schema(description = "Indicates whether traffic to this port is restricted (true) or not (false)")
    public final Boolean portRestricted;

    @Schema(description = "Indicates if the port is closed due to extreme ice conditions; icebreaker assistance is not provided")
    public final Boolean portClosed;

    @Schema(description = "Date and time of issue/announcement")
    public final ZonedDateTime issueTime;

    @Schema(description = "Date and time of last modification")
    public final ZonedDateTime lastModified;

    @Schema(description = "Date when this restriction starts/started to be in effect; this is optional if isCurrent is true and\n" +
                              "portRestricted is false, because during early winter – when no restrictions yet exist – the\n" +
                              "past restriction data (end time of past season's last restriction) might not always be available")
    public final Date validFrom;

    @Schema(description = "Date when this restriction ceased to be in effect")
    public final Date validUntil;

    @Schema(description = "Raw text of the port traffic restriction.\n" +
                              "Finnish-Swedish ice classes are IA Super, IA, IB, IC, II and I.\n" +
                              "The number following the ice class code stands for minimum required ship deadweight tonnage.\n" +
                              "'2000 t' in 'IA 4000 | 2000 t' code stands for minimum cargo the ship has to load or unload.")
    public final String rawText;

    @Schema(description = "Traffic restriction text pre-formatted (HTML)")
    public final String formattedText;

    public WinterNavigationPortRestrictionV1(final Boolean isCurrent, final Boolean portRestricted, final Boolean portClosed, final ZonedDateTime issueTime, final ZonedDateTime lastModified,
                                             final Date validFrom, final Date validUntil, final String rawText, final String formattedText) {
        this.isCurrent = isCurrent;
        this.portRestricted = portRestricted;
        this.portClosed = portClosed;
        this.issueTime = issueTime;
        this.lastModified = lastModified;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.rawText = rawText;
        this.formattedText = formattedText;
    }
}
