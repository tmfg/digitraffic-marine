package fi.livi.digitraffic.meri.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "vessel")
public class VesselMetadata {

    @Id
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    private int mmsi;

    @ApiModelProperty(value = "Name of the vessel", required = true)
    private String name;

    @ApiModelProperty(value = "Vessel type", required = true)
    private int shipType;

    @ApiModelProperty(value = "Vessel draught", required = true)
    private int draught;

    @ApiModelProperty(value = "Vessel International Maritime Organization (IMO) number", required = true)
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
