package fi.livi.digitraffic.meri.domain.ais;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.model.ais.VesselMessage;
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

    @ApiModelProperty(value = "Reference point for reported position dimension A", required = true)
    @Column(name = "reference_point_a")
    private long referencePointA;

    @ApiModelProperty(value = "Reference point for reported position dimension B", required = true)
    @Column(name = "reference_point_b")
    private long referencePointB;

    @ApiModelProperty(value = "Reference point for reported position dimension C", required = true)
    @Column(name = "reference_point_c")
    private long referencePointC;

    @ApiModelProperty(value = "Reference point for reported position dimension D", required = true)
    @Column(name = "reference_point_d")
    private long referencePointD;

    @ApiModelProperty(value = "Type of electronic position fixing device: 0 = undefined (default)\n"
            + "1 = GPS\n"
            + "2 = GLONASS\n"
            + "3 = combined GPS/GLONASS\n"
            + "4 = Loran-C\n"
            + "5 = Chayka\n"
            + "6 = integrated navigation system\n"
            + "7 = surveyed\n"
            + "8 = Galileo,\n"
            + "9-14 = not used\n"
            + "15 = internal GNSS", allowableValues = "range[0,15]", required = true)
    private int posType;

    @ApiModelProperty(value = "Maximum present static draught in 1/10m", allowableValues = "range[0,255]", required = true)
    private int draught;

    @ApiModelProperty(value = "Vessel International Maritime Organization (IMO) number", required = true)
    private int imo;

    @ApiModelProperty(value = "Call sign, maximum 7 6-bit ASCII characters", required = true)
    private String callSign;

    @ApiModelProperty(value = "Estimated time of arrival; MMDDHHMM UTC\n"
            + "Bits 19-16: month; 1-12; 0 = not available = default\n"
            + "Bits 15-11: day; 1-31; 0 = not available = default\n"
            + "Bits 10-6: hour; 0-23; 24 = not available = default\n"
            + "Bits 5-0: minute; 0-59; 60 = not available = default\n"
            + "For SAR aircraft, the use of this field may be decided by the responsible administration", required = true)
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
        this.referencePointA = attr.referencePointA;
        this.referencePointB = attr.referencePointB;
        this.referencePointC = attr.referencePointC;
        this.referencePointD = attr.referencePointD;
        this.posType = attr.posType;
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

    public void setCallSign(final String callSign) {
        this.callSign = callSign;
    }

    public long getEta() {
        return eta;
    }

    public void setEta(final long eta) {
        this.eta = eta;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPosType() {
        return posType;
    }

    public void setPosType(final int posType) {
        this.posType = posType;
    }

    public long getReferencePointA() {
        return referencePointA;
    }

    public void setReferencePointA(final long referencePointA) {
        this.referencePointA = referencePointA;
    }

    public long getReferencePointB() {
        return referencePointB;
    }

    public void setReferencePointB(final long referencePointB) {
        this.referencePointB = referencePointB;
    }

    public long getReferencePointC() {
        return referencePointC;
    }

    public void setReferencePointC(final long referencePointC) {
        this.referencePointC = referencePointC;
    }

    public long getReferencePointD() {
        return referencePointD;
    }

    public void setReferencePointD(final long referencePointD) {
        this.referencePointD = referencePointD;
    }
}
