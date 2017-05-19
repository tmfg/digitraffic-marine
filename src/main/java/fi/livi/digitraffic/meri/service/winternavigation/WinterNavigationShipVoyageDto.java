package fi.livi.digitraffic.meri.service.winternavigation;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinterNavigationShipVoyageDto {

    public final String fromLocode;

    public final String fromName;

    public final ZonedDateTime fromAtd;

    public final String inLocode;

    public final String inName;

    public final ZonedDateTime inAta;

    public final ZonedDateTime inEtd;

    public final String destLocode;

    public final String destName;

    public final ZonedDateTime destEta;

    public WinterNavigationShipVoyageDto(@JsonProperty("from_locode") final String fromLocode,
                                         @JsonProperty("from_name") final String fromName,
                                         @JsonProperty("from_atd") final ZonedDateTime fromAtd,
                                         @JsonProperty("in_locode") final String inLocode,
                                         @JsonProperty("in_name") final String inName,
                                         @JsonProperty("in_ata") final ZonedDateTime inAta,
                                         @JsonProperty("in_etd") final ZonedDateTime inEtd,
                                         @JsonProperty("dest_locode") final String destLocode,
                                         @JsonProperty("dest_name") final String destName,
                                         @JsonProperty("dest_eta") final ZonedDateTime destEta) {
        this.fromLocode = fromLocode;
        this.fromName = fromName;
        this.fromAtd = fromAtd;
        this.inLocode = inLocode;
        this.inName = inName;
        this.inAta = inAta;
        this.inEtd = inEtd;
        this.destLocode = destLocode;
        this.destName = destName;
        this.destEta = destEta;
    }
}
