package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
@DynamicUpdate
public class ShipVoyage {

    @Id
    private String id;

    @JoinColumn(name = "vessel_pk")
    @OneToOne
    @MapsId
    private WinterNavigationShip winterNavigationShip;

    private String fromLocode;

    private String fromName;

    private Instant fromAtd;

    private String inLocode;

    private String inName;

    private Instant inAta;

    private Instant inEtd;

    private String destLocode;

    private String destName;

    private Instant destEta;

    public String getFromLocode() {
        return fromLocode;
    }

    public void setFromLocode(final String fromLocode) {
        this.fromLocode = fromLocode;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public Instant getFromAtd() {
        return fromAtd;
    }

    public void setFromAtd(final Instant fromAtd) {
        this.fromAtd = fromAtd;
    }

    public String getInLocode() {
        return inLocode;
    }

    public void setInLocode(final String inLocode) {
        this.inLocode = inLocode;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(final String inName) {
        this.inName = inName;
    }

    public Instant getInAta() {
        return inAta;
    }

    public void setInAta(final Instant inAta) {
        this.inAta = inAta;
    }

    public Instant getInEtd() {
        return inEtd;
    }

    public void setInEtd(final Instant inEtd) {
        this.inEtd = inEtd;
    }

    public String getDestLocode() {
        return destLocode;
    }

    public void setDestLocode(final String destLocode) {
        this.destLocode = destLocode;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(final String destName) {
        this.destName = destName;
    }

    public Instant getDestEta() {
        return destEta;
    }

    public void setDestEta(final Instant destEta) {
        this.destEta = destEta;
    }

    public void setWinterNavigationShip(final WinterNavigationShip winterNavigationShip) {
        this.winterNavigationShip = winterNavigationShip;
    }
}
