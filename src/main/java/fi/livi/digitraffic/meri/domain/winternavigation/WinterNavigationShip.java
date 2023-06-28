package fi.livi.digitraffic.meri.domain.winternavigation;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;

import fi.livi.digitraffic.meri.domain.ReadOnlyCreatedAndModifiedFields;

@Entity
public class WinterNavigationShip extends ReadOnlyCreatedAndModifiedFields {

    @Id
    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    private String vesselSource;

    private String mmsi;

    private String name;

    private String callSign;

    private String imo;

    private Double dwt;

    private Double length;

    private Double width;

    private Double aisLength;

    private Double aisWidth;

    private String dimensions;

    private Double nominalDraught;

    private String iceClass;

    private String natCode;

    private String nationality;

    private String shipType;

    private Integer aisShipType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "winterNavigationShip")
    @JoinColumn(name = "vessel_pk", nullable = false)
    private ShipState shipState;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "winterNavigationShip")
    @JoinColumn(name = "vessel_pk", nullable = false)
    private ShipVoyage shipVoyage;

    @OneToMany(mappedBy = "vesselPK", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "orderNumber")
    private Set<ShipActivity> shipActivities = new HashSet<>();

    @OneToMany(mappedBy = "vesselPK", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "orderNumber")
    private Set<ShipPlannedActivity> shipPlannedActivities = new HashSet<>();

    public String getMmsi() {
        return mmsi;
    }

    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public String getVesselSource() {
        return vesselSource;
    }

    public void setVesselSource(String vesselSource) {
        this.vesselSource = vesselSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

    public Double getDwt() {
        return dwt;
    }

    public void setDwt(Double dwt) {
        this.dwt = dwt;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getAisLength() {
        return aisLength;
    }

    public void setAisLength(Double aisLength) {
        this.aisLength = aisLength;
    }

    public Double getAisWidth() {
        return aisWidth;
    }

    public void setAisWidth(Double aisWidth) {
        this.aisWidth = aisWidth;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Double getNominalDraught() {
        return nominalDraught;
    }

    public void setNominalDraught(Double nominalDraught) {
        this.nominalDraught = nominalDraught;
    }

    public String getIceClass() {
        return iceClass;
    }

    public void setIceClass(String iceClass) {
        this.iceClass = iceClass;
    }

    public String getNatCode() {
        return natCode;
    }

    public void setNatCode(String natCode) {
        this.natCode = natCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Integer getAisShipType() {
        return aisShipType;
    }

    public void setAisShipType(Integer aisShipType) {
        this.aisShipType = aisShipType;
    }

    public ShipState getShipState() {
        return shipState;
    }

    public void setShipState(ShipState shipState) {
        this.shipState = shipState;
    }

    public ShipVoyage getShipVoyage() {
        return shipVoyage;
    }

    public void setShipVoyage(ShipVoyage shipVoyage) {
        this.shipVoyage = shipVoyage;
    }

    public Set<ShipActivity> getShipActivities() {
        return shipActivities;
    }

    public Set<ShipPlannedActivity> getShipPlannedActivities() {
        return shipPlannedActivities;
    }
}
