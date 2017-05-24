package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShipVoyage {

    @Id
    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    private String fromLocode;

    private String fromName;

    private Timestamp fromAtd;

    private String inLocode;

    private String inName;

    private Timestamp inAta;

    private Timestamp inEtd;

    private String destLocode;

    private String destName;

    private Timestamp destEta;

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public String getFromLocode() {
        return fromLocode;
    }

    public void setFromLocode(String fromLocode) {
        this.fromLocode = fromLocode;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Timestamp getFromAtd() {
        return fromAtd;
    }

    public void setFromAtd(Timestamp fromAtd) {
        this.fromAtd = fromAtd;
    }

    public String getInLocode() {
        return inLocode;
    }

    public void setInLocode(String inLocode) {
        this.inLocode = inLocode;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(String inName) {
        this.inName = inName;
    }

    public Timestamp getInAta() {
        return inAta;
    }

    public void setInAta(Timestamp inAta) {
        this.inAta = inAta;
    }

    public Timestamp getInEtd() {
        return inEtd;
    }

    public void setInEtd(Timestamp inEtd) {
        this.inEtd = inEtd;
    }

    public String getDestLocode() {
        return destLocode;
    }

    public void setDestLocode(String destLocode) {
        this.destLocode = destLocode;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public Timestamp getDestEta() {
        return destEta;
    }

    public void setDestEta(Timestamp destEta) {
        this.destEta = destEta;
    }
}
