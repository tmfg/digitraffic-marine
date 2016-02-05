package fi.livi.digitraffic.meri.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "vessel")
public class VesselMetadata {
    @Id
    private int mmsi;
    private String name;
    private int shipType;
    private int draught;
    private int imo;

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(final int mmsi) {
        this.mmsi = mmsi;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(final int shipType) {
        this.shipType = shipType;
    }

    public int getDraught() {
        return draught;
    }

    public void setDraught(final int draught) {
        this.draught = draught;
    }

    public int getImo() {
        return imo;
    }

    public void setImo(final int imo) {
        this.imo = imo;
    }
}
