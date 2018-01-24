package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PortRestrictionDto {

    public final Boolean isCurrent;

    public final Boolean portRestricted;

    public final Boolean portClosed;

    public final ZonedDateTime issueTime;

    public final ZonedDateTime timeStamp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public final Date validFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public final Date validUntil;

    public final String rawText;

    public final String formattedText;

    public PortRestrictionDto(@JsonProperty("isCurrent") final Boolean isCurrent,
                              @JsonProperty("portRestricted") final Boolean portRestricted,
                              @JsonProperty("portClosed") final Boolean portClosed,
                              @JsonProperty("issueTime") final ZonedDateTime issueTime,
                              @JsonProperty("timeStamp") final ZonedDateTime timeStamp,
                              @JsonProperty("validFrom") final Date validFrom,
                              @JsonProperty("validUntil") final Date validUntil,
                              @JsonProperty("rawText") final String rawText,
                              @JsonProperty("formattedText") final String formattedText) {
        this.isCurrent = isCurrent;
        this.portRestricted = portRestricted;
        this.portClosed = portClosed;
        this.issueTime = issueTime;
        this.timeStamp = timeStamp;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.rawText = rawText;
        this.formattedText = formattedText;
    }
}
