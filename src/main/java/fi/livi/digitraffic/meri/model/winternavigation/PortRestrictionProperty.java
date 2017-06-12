package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PortRestriction")
public class PortRestrictionProperty {

    @ApiModelProperty(value = "Indicates whether this restriction is currently in effect (true); false implies past/future restriction")
    public final Boolean isCurrent;

    @ApiModelProperty(value = "Indicates whether traffic to this port is restricted (true) or not (false)")
    public final Boolean portRestricted;

    @ApiModelProperty(value = "Indicates if the port is closed due to extreme ice conditions; icebreaker assistance is not provided")
    public final Boolean portClosed;

    @ApiModelProperty(value = "Date and time of issue/announcement")
    public final Timestamp issueTime;

    @ApiModelProperty(value = "Date and time of last modification")
    public final Timestamp lastModified;

    @ApiModelProperty(value = "Date when this restriction starts/started to be in effect; this is optional if isCurrent is true and\n" +
                              "portRestricted is false, because during early winter – when no restrictions yet exist – the\n" +
                              "past restriction data (end time of past season's last restriction) might not always be available")
    public final Date validFrom;

    @ApiModelProperty(value = "Date when this restriction ceased to be in effect")
    public final Date validUntil;

    @ApiModelProperty(value = "Raw text of the port traffic restriction.\n" +
                              "Finnish-Swedish ice classes are IA Super, IA, IB, IC, II and I.\n" +
                              "The number following the ice class code stands for minimum required ship deadweight tonnage.\n" +
                              "'2000 t' in 'IA 4000 | 2000 t' code stands for minimum cargo the ship has to load or unload.")
    public final String rawText;

    @ApiModelProperty(value = "Traffic restriction text pre-formatted (HTML)")
    public final String formattedText;

    public PortRestrictionProperty(Boolean isCurrent, Boolean portRestricted, Boolean portClosed, Timestamp issueTime, Timestamp lastModified,
                                   Date validFrom, Date validUntil, String rawText, String formattedText) {
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
