package fi.livi.digitraffic.meri.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.model.VesselMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "vessel")
@DynamicUpdate
@ApiModel(description="Vessel metadata model")
public class VesselMetadata {
    @Id
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    private int mmsi;

    @ApiModelProperty(value = "Name of the vessel, maximum 20 characters using 6-bit ASCII", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "Vessel type", allowableValues = "range[0,255]", required = true)
    private int shipType;

    @ApiModelProperty(value = "Maximum present static draught in 1/10m", allowableValues = "range[1-254], 0, 255", required = true)
    private int draught;

    @ApiModelProperty(value = "Vessel International Maritime Organization (IMO) number", required = true)
    private int imo;

    @ApiModelProperty(value = "Call sign, maximum 7 6-bit ASCII characters", required = true)
    private String callSign;

    @ApiModelProperty(value = "Estimated time of arrival", required = true)
    private long eta;

    @ApiModelProperty(value = "Record timestamp in milliseconds from Unix epoch", required = true)
    private long timestamp;

    @ApiModelProperty(value = "Destination, maximum 20 characters using 6-bit ASCII", required = true)
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
