package fi.livi.digitraffic.meri.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.model.VesselMessage;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "vessel")
@DynamicUpdate
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

    private String callSign;

    private long eta;

    private long timestamp;

    private String destination;

    public  VesselMetadata() {
        // for hibernate
    }

    public VesselMetadata(final VesselMessage.VesselAttributes attr) {
        this.mmsi = attr.mmsi;
        this.name = attr.vesselName;
        this.shipType = attr.shipAndCargoType;
        this.draught = attr.draught;
        this.imo = attr.imo;
        this.callSign = attr.callSign;
        this.eta = attr.eta;
        this.timestamp = attr.timestamp;
        this.destination = attr.dest;
    }

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

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public long getEta() {
        return eta;
    }

    public void setEta(long eta) {
        this.eta = eta;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
