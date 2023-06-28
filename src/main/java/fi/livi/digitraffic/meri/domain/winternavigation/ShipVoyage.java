package fi.livi.digitraffic.meri.domain.winternavigation;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class ShipVoyage {

    @Id
    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    @OneToOne
    @JoinColumn(name = "vessel_pk", nullable = false)
    @MapsId
    private WinterNavigationShip winterNavigationShip;

    private String fromLocode;

    private String fromName;

    private ZonedDateTime fromAtd;

    private String inLocode;

    private String inName;

    private ZonedDateTime inAta;

    private ZonedDateTime inEtd;

    private String destLocode;

    private String destName;

    private ZonedDateTime destEta;

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

    public ZonedDateTime getFromAtd() {
        return fromAtd;
    }

    public void setFromAtd(ZonedDateTime fromAtd) {
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

    public ZonedDateTime getInAta() {
        return inAta;
    }

    public void setInAta(ZonedDateTime inAta) {
        this.inAta = inAta;
    }

    public ZonedDateTime getInEtd() {
        return inEtd;
    }

    public void setInEtd(ZonedDateTime inEtd) {
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

    public ZonedDateTime getDestEta() {
        return destEta;
    }

    public void setDestEta(ZonedDateTime destEta) {
        this.destEta = destEta;
    }

    public void setWinterNavigationShip(final WinterNavigationShip winterNavigationShip) {
        this.winterNavigationShip = winterNavigationShip;
        this.vesselPK = winterNavigationShip.getVesselPK();
    }
}
