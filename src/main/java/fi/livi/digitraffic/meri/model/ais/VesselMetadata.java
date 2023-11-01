package fi.livi.digitraffic.meri.model.ais;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselMetadataJsonV1;
import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "vessel")
@DynamicUpdate
public class VesselMetadata extends ReadOnlyCreatedAndModifiedFields implements VesselMetadataJsonV1 {
    @Id
    private int mmsi;

    @NotNull
    private String name;

    private int shipType;

    @Column(name = "reference_point_a")
    private long referencePointA;

    @Column(name = "reference_point_b")
    private long referencePointB;

    @Column(name = "reference_point_c")
    private long referencePointC;

    @Column(name = "reference_point_d")
    private long referencePointD;

    private int posType;

    private int draught;

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

    @Override
    public Instant getLastModified() {
        return getModified();
    }
}
