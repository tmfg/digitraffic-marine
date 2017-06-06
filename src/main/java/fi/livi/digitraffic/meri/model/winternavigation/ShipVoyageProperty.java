package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;

public class ShipVoyageProperty {

    public final String fromLocode;

    public final String fromName;

    public final Timestamp fromAtd;

    public final String inLocode;

    public final String inName;

    public final Timestamp inAta;

    public final Timestamp inEtd;

    public final String destLocode;

    public final String destName;

    public final Timestamp destEta;

    public ShipVoyageProperty(String fromLocode, String fromName, Timestamp fromAtd, String inLocode, String inName, Timestamp inAta, Timestamp inEtd,
                              String destLocode, String destName, Timestamp destEta) {
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
